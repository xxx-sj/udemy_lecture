<template>
  <base-container title="Vuex" v-if='isAuth'>
    <the-counter />
    <favorite-value />
    <button @click='addOne'>Add 10</button>
    <change-counter />
  </base-container>
  <base-container title='Auth'>
    <user-auth />
  </base-container>
</template>

<script>
import BaseContainer from './components/BaseContainer.vue';
import TheCounter from '@/components/TheCounter';
import ChangeCounter from '@/components/ChangeCounter';
import FavoriteValue from '@/components/FavoriteValue';
import userAuth from '@/components/UserAuth';

export default {
  components: {
    BaseContainer, TheCounter, ChangeCounter, FavoriteValue, userAuth
  },
  computed: {
    isAuth() {
      return this.$store.getters.userIsAuthenticated
    }
  },
  methods: {
    addOne() {
      // this.$store.commit('increase', {value: 10});
      /*다른 방법*/
      this.$store.dispatch({
        type: 'numbers/increase',
        value: 10,
      })
    }
  }
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
</style>
