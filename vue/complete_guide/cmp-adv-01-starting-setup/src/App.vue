<template>
  <div>
    <the-header></the-header>
    <button @click="setSelectedComponent('active-goals')">Active goals</button>
    <button @click="setSelectedComponent('manage-goals')">Manage goals</button>
<!--    <active-goals v-if="selectedComponents === 'active-goals'"></active-goals>-->
<!--    <manage-goals v-if="selectedComponents === 'manage-goals'"></manage-goals>-->
    <keep-alive>
      <component :is="selectedComponents"></component>
    </keep-alive>
  </div>
</template>

<script>
import TheHeader from '@/components/TheHeader'
import ActiveGoals from "@/components/ActiveGoals";
import ManageGoals from "@/components/ManageGoals";

export default {
  components: {
    TheHeader,
    ActiveGoals,
    ManageGoals,
  },
  data() {
    return {
      selectedComponents: 'active-goals',
      activeUser: {
        name: 'Maximilian Schwarzmüller',
        description: 'Site owner and admin',
        role: 'admin',
      },
    };
  },
  methods: {
    setSelectedComponent(cmp) {
      this.selectedComponents = cmp;
    }
  }
};
</script>

<style>
html {
  font-family: sans-serif;
}

body {
  margin: 0;
}
</style>
