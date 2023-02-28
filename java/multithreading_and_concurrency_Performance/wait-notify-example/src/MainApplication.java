/*
 * MIT License
 *
 * Copyright (c) 2019 Michael Pogrebinsky
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * Objects as Condition Variables - Wait(), Notify() and NotifyAll()
 * https://www.udemy.com/java-multithreading-concurrency-performance-optimization
 */
public class MainApplication {
    private static final String INPUT_FILE = "./out/matrices";
    private static final String OUTPUT_FILE = "./out/matrices_results.txt";
    private static final int N = 10;

    public static void main(String[] args) throws IOException {
        ThreadSafeQueue threadSafeQueue = new ThreadSafeQueue();
        File inputFile = new File(INPUT_FILE);
        File outputFile = new File(OUTPUT_FILE);

        MatricesReaderProducer matricesReader = new MatricesReaderProducer(new FileReader(inputFile), threadSafeQueue);
        MatricesMultiplierConsumer matricesConsumer = new MatricesMultiplierConsumer(new FileWriter(outputFile), threadSafeQueue);

        matricesConsumer.start();
        matricesReader.start();
    }

    //소비자 클래스
    private static class MatricesMultiplierConsumer extends Thread {
        private ThreadSafeQueue queue;
        private FileWriter fileWriter;

        public MatricesMultiplierConsumer(FileWriter fileWriter, ThreadSafeQueue queue) {
            this.fileWriter = fileWriter;
            this.queue = queue;
        }

        //헬퍼 메서드 filewriter과 결과 행렬을 갖는 method
        private static void saveMatrixToFile(FileWriter fileWriter, float[][] matrix) throws IOException {
            //행렬의 모든 행을 반복
            for (int r = 0; r < N; r++) {
                //행을 반복할 때마다 쉼표로 모든 요소를 연결
                StringJoiner stringJoiner = new StringJoiner(", ");
                //행의 모든 요소를 검토
                for (int c = 0; c < N; c++) {
                    //add method 호출해 연결한다.
                    stringJoiner.add(String.format("%.2f", matrix[r][c]));
                }
                //행 끝의 fileWriter에 행을 쓰고
                fileWriter.write(stringJoiner.toString());
                //행을 구획하기 위해 다음 라인으로 간다.
                fileWriter.write('\n');
            }
            //두 행렬 구분
            fileWriter.write('\n');
        }

        @Override
        public void run() {
            while (true) {
                //큐의 행렬 쌍 소비
                //생산자가 아무것도 생산하지 않은 경우 메서드를 블록함.
                MatricesPair matricesPair = queue.remove();
                //소비한 요소가 null이면 break 으로 종료
                if (matricesPair == null) {
                    System.out.println("No more matrices to read from the queue, consumer is terminating");
                    break;
                }

                //null이 아니면 방금 구현한 multiply[행렬곱셈] 메서드 call
                float[][] result = multiplyMatrices(matricesPair.matrix1, matricesPair.matrix2);

                try {
                    //결과를 파일에 저장
                    saveMatrixToFile(fileWriter, result);
                } catch (IOException e) {
                }
            }

            //모든 행렬 곱셈을 마치고 스레드에서 나오기 전에
            // 파일을 flush하고 close해야 한다.
            //파일 시스템에 데이터를 정확히 쓰기 위해서
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private float[][] multiplyMatrices(float[][] m1, float[][] m2) {
            //결과 저장 변수
            float[][] result = new float[N][N];

            //행렬 곱셈 m1 x m2
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {
                    for (int k = 0; k < N; k++) {
                        result[r][c] += m1[r][k] * m2[k][c];
                    }
                }
            }
            return result;
        }
    }

    //생산자는 파일의 행렬을 쉽게 읽고 분석하게 하는 scanner class를 갖는다.
    private static class MatricesReaderProducer extends Thread {
        private Scanner scanner;
        private ThreadSafeQueue queue;

        public MatricesReaderProducer(FileReader reader, ThreadSafeQueue queue) {
            this.scanner = new Scanner(reader);
            this.queue = queue;
        }

        //생산자 작업
        @Override
        public void run() {
            while (true) {
                //2개의 행렬을 읽는다.
                float[][] matrix1 = readMatrix();
                float[][] matrix2 = readMatrix();

                //둘 중 하나가 null이면 입력 파일 끝으로 가서
                if (matrix1 == null || matrix2 == null) {
                    //큐에 terminate메서드를 호출 해 "읽을 행렬이 더는 없다"를 출력
                    queue.terminate();
                    System.out.println("No more matrices to read. Producer Thread is terminating");
                    //스레드를 끝낸다.
                    return;
                }

                //있다면 분석한 행렬을 객체에 저장한다.
                MatricesPair matricesPair = new MatricesPair();
                matricesPair.matrix1 = matrix1;
                matricesPair.matrix2 = matrix2;

                //그리고 객체를 큐에 추가하고 반복할 다음쌍으로 넘어간다.
                queue.add(matricesPair);
            }
        }

        /**
         * 파일의 단일 행렬을 분석하여 부동소수의 2차원 배열로 전환시킨다.
         * 정사각형의 10x10 행렬을 다루기에 상수를 코드 상단에 저장한다. [N]
         *
         */
        private float[][] readMatrix() {
            //2차원 배열 인스턴스화한 다음
            float[][] matrix = new float[N][N];
            //0~ n-1 사이의 모든 행에 루프작업
            for (int r = 0; r < N; r++) {
                //모든 행에서 우선 읽을 콘텐츠가 파일에 있는지 확인
                //없으면 입력 끝
                if (!scanner.hasNext()) {
                    return null;
                }
                //있는 경우 파일의 다음 라인을 받아 쉼표로 분할 해
                //58.35, 32.97, 4.08, 30.85, 0.00, 47.08, 13.48, 6.55, 3.91, 5.88
                //행의 행렬 요소를 표현하는 문자열 배열을 갖는다.
                String[] line = scanner.nextLine().split(",");
                // 행의 모든 요소에 루프 작업을 하여
                for (int c = 0; c < N; c++) {
                    //String -> float으로 변경해서 2차원 배열[위에 생성한]에 저장
                    matrix[r][c] = Float.valueOf(line[c]);
                }
            }
            //다음 위치로 옮겨 파일에서 분석한 행렬을 반환하고
            scanner.nextLine();
            return matrix;
        }
    }

    //producer와 consumer 사이의 공유 큐를 표현하는 class
    //백프레셔 없이 간단한 큐로 시작해 어떻게 되는지 확인
    private static class ThreadSafeQueue {
        //linkedlist is not thread safe [행렬을 지니기 위한 queue]
        private Queue<MatricesPair> queue = new LinkedList<>();
        // 큐에 행렬이 포함되었는지 알려주는 변수
        private boolean isEmpty = true;
        // 생산자가 제공할 것이 없어 소비자가 스레드를 종료해야 한다는 신호를 주는 변수
        private boolean isTerminate = false;

        //큐의 최적 용량 설정
        private static final int CAPACITY = 5;

        /**
         * TODO 아래 3 메소드는 큐에 있는 작업을 원자적[synchronized]으로 유지하고
         * 서로 동기화되고 메서드 내부에서 wait(), notify() 메서드를 사용할 수 있다.
         *
         *
         */
        //큐에 행렬 쌍을 추가하기 위해 생산자가 호출하는 add 메소드
        public synchronized void add(MatricesPair matricesPair) {
            //TODO 추가된 코드.
            //while루프를 통해 최적용량에 도달했는지 확인하기.
            while (queue.size() == CAPACITY) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            queue.add(matricesPair); // queue에 추가하고
            isEmpty = false;//비었는지에 대한 bool 값을 false로 바꾼다.
            notify();//만약 기다리고있는 conumer가 있다면 깨운다.
        }

        //소비자가 큐에서 행렬 쌍을 소비해 제거하기 위해 호출하는 메소드
        public synchronized MatricesPair remove() {
            //TODO 추가된 코드
            //행렬 쌍을 소비하기 전 변수에 저장.
            MatricesPair matricesPair = null;
            while (isEmpty && !isTerminate) { //먼저 비었는지 확인하고, 생상자에 의해 종료되지 않게 해야 한다.
                //모든 조건이 true이면, [비어있고, 종료되지 않았다면,]
                //그 순가은 consumer는 소비할 것이 queue에 없다.
                try {
                    //소비할 것이없으니 wait method를 호출한다.
                    //그리고 큐에 lock을 릴리스 한다.
                    wait();
                } catch (InterruptedException e) {
                }
            }
            //consumer가 wait state에서 깨어나면,
            //condition이 더이상 충족되지 않으면, loop를 나와 if문을 실행한다.
            if (queue.size() == 1) {
                //1개라면 소비할 것이기 때문에 true로 변경
                isEmpty = true;
            }

            //이미 0이라면, 종료하는 걸로 지시 받았기에 단순히 null 반환
            if (queue.size() == 0 && isTerminate) {
                return null;
            }

            //return 되지 않았다면,
            System.out.println("queue size " + queue.size());

            //remove를 통해 matircPair를 반환한다.
            matricesPair = queue.remove();
            //queue를 소비한 이후 큐 크기가 1만큼 작아졌는지 확인한다.
            if (queue.size() == CAPACITY - 1) {
                //생산자를 깨운다.
                notifyAll();
            }
            return matricesPair;
        }

        //큐가 비면 소비자가 스레드를 종료해야한다는 사실을 알려주기 위해
        //생산자가 호출하는 메소드
        public synchronized void terminate() {
            isTerminate = true;
            notifyAll();
        }

        //세 가지 메서드 모두 동기화되었는데,
        // 대기열 원자에 대한 작업을 유지하고 방법을 사용할 수 있습니다.
    }

    //두 쌍의 행렬을 나타내는 class
    private static class MatricesPair {
        public float[][] matrix1;
        public float[][] matrix2;
    }
}
