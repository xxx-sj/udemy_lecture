<template>
  <ul>
    <li v-for="goal in goals" :key="goal">
      <slot v-bind:item="goal" another-prop="..." v-bind:test="oneData"></slot>
    </li>
  </ul>
</template>

<script>
export default {
  name: "CourseGoals",
  data() {
    return {
      goals: ['finish the course', 'learn vue'],
      oneData: [],
    };
  }
}
</script>
