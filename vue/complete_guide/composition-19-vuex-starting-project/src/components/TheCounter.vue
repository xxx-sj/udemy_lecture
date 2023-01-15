<template>
  <h2>{{ counter }}</h2>
</template>

<script>
import { computed} from "vue";
import { useStore } from 'vuex'
export default {
  setup() {
    const counter = computed(function () {
      return store.getters.counter;
    });
    const store = useStore();

    return { counter };
  },
};
</script>
