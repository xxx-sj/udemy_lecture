public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Code that will run in a new thread
                System.out.println("We are in thread(Run)" + Thread.currentThread().getName());
                System.out.println("current thread priority is 2" + Thread.currentThread().getPriority());
                throw new RuntimeException("Intentional Exception");
            }
        });

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("A critical error happend in thread" + t.getName()
                + " the error is " + e.getMessage());
            }
        });
        thread.setName("New worker Thread2");
        thread.setPriority(Thread.MAX_PRIORITY);

        /**
         * Thread.currentThread(): 현재 스레드를 가져 온다
         *      .getName() : 현재 스레드의 이름
         */
        System.out.println("WE are in thread: " + Thread.currentThread().getName() + "before starting a new thread");
        /**
         * start를 call하면 jvm에게 새 스레드를 생성하고
         * 운영 제체에 전달하도록 지시한다.
         */
        thread.start();

        System.out.println("WE are in thread: " + Thread.currentThread().getName() + "after starting a new thread");

        /**
         * 현재 실행중인 thread를 10초간 sleep 한다.
         * os에게 값으로 넘긴 millis 만큼 현재 스레드를 스케쥴하지 않는다.
         * 그 시간동안 thread는 cpu를 사용하지 않게 된다.
         */
        Thread.sleep(10000);
    }
}
