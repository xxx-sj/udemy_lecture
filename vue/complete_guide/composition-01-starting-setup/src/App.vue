<template>
  <section class="container">
    <user-data :first-name="firstName" :last-name="lastName"
    class="test">
    </user-data>
    <button @click="setAge">Change Age</button>
    <div>
      <input type="text" placeholder="First Name" v-model="firstName"/>
      <input type="text" placeholder="Last Name" ref="lastNameInput"/>
      <button @click="setLastName">Set Last Name</button>
    </div>
  </section>
</template>

<script>
import { ref, reactive, computed, watch, provide } from 'vue'
import UserData from "@/components/UserData";
export default {
  components: {UserData},
  setup() {
    // const uName = ref('Maximilian');
    const uAge = ref(31);
    const firstName = ref('');
    const lastName = ref('');
    const lastNameInput = ref(null);
    const user = reactive({
      name: "Maximilian",
      age: 31,
    });

    provide('userAge', uAge);

    const uName = computed(function() {
      return firstName.value + ' ' + lastName.value;
    });

    watch([uAge, uName], (newValues, oldValues) => {
      console.log("old age:" + oldValues[0]);
      console.log("new Age:" + newValues[0]);
      console.log("old name:" + oldValues[1]);
      console.log("new name:" + newValues[1]);
    });

    // uName.value = 'Max';

    function setNewAge() {
      user.age = 32;
      uAge.value = 33;
    }

    function setLastName() {
      console.log("lastNameInput.value", lastNameInput.value);
      lastName.value = lastNameInput.value.value;
    }

    // function setFirstName(event) {
    //   firstName.value = event.target.value;
    // }
    //
    // function setLastName(event) {
    //   lastName.value = event.target.value;
    // }



    return { age: uAge,
      user: user,
      setAge: setNewAge,
      firstName,
      lastName,
      lastNameInput,
      setLastName,
      userName: uName }
  },

  // date() {
  //   return {
  //     userName: 'Maximilian',
  //     age: 31
  //   }
  // },

  // methods: {
  //   setNewAge() {
  //     this.age = 32;
  //   },
  // },

  // computed: {},

  // watch: {
  // age(val) {
  //  console.log(val);
//    }
  // },

  // provide() {
  //   return {age: this.age}
  // }
};
</script>

<style>
* {
  box-sizing: border-box;
}

html {
  font-family: sans-serif;
}

body {
  margin: 0;
}

.container {
  margin: 3rem auto;
  max-width: 30rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.26);
  padding: 1rem;
  text-align: center;
}
</style>
