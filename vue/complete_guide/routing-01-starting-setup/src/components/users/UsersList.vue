<template>
  <button @click='confirmInput'>Confirm</button>
  <button @click='saveChanges'>Save changes</button>
  <ul>
    <user-item v-for="user in users" :key="user.id" :name="user.fullName" :role="user.role"></user-item>
  </ul>
</template>

<script>
import UserItem from './UserItem.vue';

export default {
  components: {
    UserItem,
  },
  inject: ['users'],
  data() {
    return {
      changesSaved: false,
    }
  },
  methods: {
    confirmInput() {
      //do something
      this.$router.push('/teams');
    },
    saveChanges() {
      this.changesSaved = true;
    }
  },
  beforeRouteEnter(to, from, next) {
    console.log("userList  cmp beforeRouteEnter");
    console.log(to, from);
    next();
  },
  beforeRouteLeave(to, from, next) {
    console.log('userList cmp beforeRouteLeave');
    console.log(to, from);
    if (this.changesSaved) {
      next();
    } else {
      let userWantsToLeave = confirm('Are tou sure? You got unsaved changes!');
      next(userWantsToLeave);
    }

  },
  unmounted() {
    console.log('unmounted');
  },
};
</script>

<style scoped>
ul {
  list-style: none;
  margin: 2rem auto;
  max-width: 20rem;
  padding: 0;
}
</style>
