<template>
  <section>
    <base-card>
      <h2>Page not fount</h2>
      <p>This page could not be found - maybe check out all out
        <router-link to='/coaches'>coaches</router-link>
      </p>
    </base-card>
  </section>
</template>

<script>
import BaseCard from '@/components/UI/BaseCard';
export default {
  name: 'NotFound',
  components: { BaseCard }
};
</script>

<style scoped>

</style>
