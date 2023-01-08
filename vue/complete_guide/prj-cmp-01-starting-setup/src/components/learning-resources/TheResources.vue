<template>
  <base-card>
    <base-button @click='setSelectedTab("stored-resources")' :mode='storedResButtonMode'>Stored Resources</base-button>
    <base-button @click='setSelectedTab("add-resource")' :mode='addResButtonMode'>Add Resource</base-button>
  </base-card>
  <keep-alive>
    <component :is='selectedTab'/>
  </keep-alive>
</template>

<script>
import StoredResources from './StoredResources';
import AddResource from '@/components/learning-resources/AddResource';
export default {
  name: 'TheResources',
  components: {
    StoredResources,
    AddResource,
  },
  data() {
    return {
      selectedTab: 'stored-resources',
      storedResources: [
        { id: 'official-guide',
          title: 'Official Guide',
          description: 'The official Vue.js documentation',
          link: 'https://vuejs.org'
        },
        { id: 'google',
          title: 'Google',
          description: 'Learn to google...',
          link: 'https://google.com'
        },
      ],
    };
  },
  provide() {
    return {
      resources: this.storedResources,
      addResource: this.addResource,
      deleteResource: this.removeResource,
    }
  },
  computed: {
    storedResButtonMode() {
      return this.selectedTab === "stored-resources" ? null : "flat";
    },
    addResButtonMode() {
      return this.selectedTab === "add-resource" ? null : "flat";
    }
  },
  methods: {
    setSelectedTab(tab) {
      this.selectedTab = tab;
    },
    addResource(title, description, url) {
      const newResource = {
        id: new Date().toISOString(),
        title,
        description,
        url, };

      this.storedResources.unshift(newResource);
      this.selectedTab = 'stored-resources';
    },

    removeResource(resId) {
      // this.storedResources = this.storedResources.filter((res) => res.id !== resId);
      //
      // console.log(this.storedResources);
      const resIndex = this.storedResources.findIndex(res => res.id === resId);
      this.storedResources.splice(resIndex, 1);
    }
  }
};
</script>

<style scoped>

</style>
