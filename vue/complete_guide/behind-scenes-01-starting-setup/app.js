const app = Vue.createApp({
  data() {
    return {
      currentUserInput: '',
      message: 'Vue is great!',
    };
  },
  methods: {
    saveInput(event) {
      this.currentUserInput = event.target.value;
    },
    setText() {
      // this.message = this.currentUserInput;
      this.message = this.$refs.userText
    },
  },

  beforeCreate() {
    console.log("beforeCreated");
  },

  created() {
    console.log("created");
  },

  beforeMount() {
    console.log('beforeMount');
  },

  mounted() {
    console.log('mounted');
  }
});

app.mount('#app');


const app2 = Vue.createApp({
  template: `
    <p>{{favoriteMeal}}</p>
  `,
  data() {
    return {
      favoriteMeal: "Pizza"
    };
  },
});

app2.mount("#app2")



// ...

let message = "Hello";

let longMessage = message + "World!";
// console.log(longMessage)

message = "Hello!!!!!";
// console.log(longMessage)

//....

const data = {
  message: "Hello!",
  longMessage: "Hello! World!",
};

const handler = {
  set(target, key, value) {
    if (key === 'message') {
      target.longMessage = value + ' World!';
    }
    target.message = value;
  }
};

const proxy = new Proxy(data, handler);
console.log(proxy.longMessage)

proxy.message = 'Hello!!!!';

console.log(proxy.longMessage)
