const app = Vue.createApp({
  data() {
    return {
      counter: 10,
      confirmedName: '',
      name:  '',
      fullname: '',
    };
  },

  watch: {
    name(value) {
      if (value === '') {
        this.fullname = '';
      } else {
        this.fullname = value + " " + "last name";
      }
    }
  },

  computed: {
    fullname() {
      console.log("method called");
      if (this.name === '') {
        return '';
      }
      return this.name + " last name";
    }
  },

  methods: {
    outpuFullname() {
      console.log("method called");
      if (this.name === '') {
        return '';
      }
      return this.name + "last name";
    },
    setName(event) {
      this.name = event.target.value
    },
    confirmInput() {
      this.confirmedName = this.name;
    },
    submitForm(event) {
      event.preventDefault();
      alert("Submitted!");
    },
  }
});

app.mount('#events');
