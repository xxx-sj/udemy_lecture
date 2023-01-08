<template>
  <div>
    <base-dialog :show='!!error' title='An error occurred!' @close='handleError'>
      <p>{{ error }}</p>
    </base-dialog>
    <section>
      <coach-filter @change-filter='setFilter'></coach-filter>
    </section>
    <section>
      <base-card>
        <div class='controls'>
          <base-button mode='outline' @click='loadCoaches(true)'>Refresh</base-button>
          <base-button link to='/auth?redirect=register' v-if='!isLoggedIn'>Login to Register as Coach</base-button>
          <base-button v-if='isLoggedIn && !isCoach && !isLoading' link to='/register'>Register as Coach</base-button>
        </div>
        <div v-if='isLoading'>
          <base-spinner />
        </div>
        <ul v-else-if='hasCoaches'>
          <coach-item v-for='coach in filteredCoaches' :key='coach.id'
                      :id='coach.id'
                      :first-name='coach.firstName'
                      :last-name='coach.lastName'
                      :rate='coach.hourlyRate'
                      :areas='coach.areas'
          />
        </ul>
        <h3 v-else>No caches Found.</h3>
      </base-card>
    </section>
  </div>
</template>

<script>
import CoachItem from '@/components/coaches/CoachItem';
import CoachFilter from '@/components/coaches/CoachFilter';
import BaseSpinner from '@/components/UI/BaseSpinner';
import BaseButton from '@/components/UI/BaseButton';

export default {
  name: 'CoachesList',
  components: { BaseButton, BaseSpinner, CoachItem, CoachFilter },
  computed: {
    // ...mapGetters('coaches', {
    //   filteredCoaches: 'coaches',
    //   hasCoaches: 'hasCoaches'
    // })
    isLoggedIn() {
      return this.$store.getters['auth/isAuthenticated'];
    },
    filteredCoaches() {
      const coaches = this.$store.getters['coaches/coaches']
      return coaches.filter( coach => {
        if (this.activeFilters.frontend && coach.areas.includes('frontend')) {
          return true;
        }
        if (this.activeFilters.backend && coach.areas.includes('backend')) {
          return true;
        }
        if (this.activeFilters.career && coach.areas.includes('career')) {
          return true;
        }
        return false;
      })
    },
    hasCoaches() {
      return !this.isLoading && this.$store.getters['coaches/hasCoaches']
    },
    isCoach() {
      return this.$store.getters['coaches/isCoach']
    }
  },
  data() {
    return {
      isLoading: false,
      error: null,
      activeFilters: {
        frontend: true,
        backend: true,
        career: true,
      }
    }
  },
  created() {
    this.loadCoaches(false);
  },
  methods: {
    setFilter(updatedFilters) {
      this.activeFilters = updatedFilters;
    },

    async loadCoaches(refresh = false) {
      this.isLoading = true;
      try {
        await this.$store.dispatch('coaches/loadCoaches', {forceRefresh: refresh});
      } catch(error) {
        this.error = error.message || 'Something went wrong!';
      }
      this.isLoading = false;

    },
    handleError() {
      this.error = null;
    }
  },
};
</script>

<style scoped>
ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.controls {
  display: flex;
  justify-content: space-between;
}
</style>
