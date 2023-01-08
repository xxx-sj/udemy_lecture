<template>
  <li>
    <h2>{{ name }}</h2>
    <button @click="toggleFavorite">{{isFavorite ? '(favorite)': ''}}toggle favorite</button>
    <button @click="toggleDetails">{{detailsAreVisible ? 'Hide' : 'Show'}} Details</button>
    <ul v-if="detailsAreVisible">
      <li><strong>phone: </strong> {{ phoneNumber }}</li>
      <li><strong>email: </strong> {{ emailAddress }}</li>
    </ul>
    <button @click="$emit('delete', id)">Delete</button>
  </li>
</template>

<script>
export default {
  name: "FriendContact",
  // props: ['name', 'phoneNumber', 'emailAddress'],
  props: {
    id: {
      type: String,
      required: true,
    },
    name: {
      type: String,
      required: true,
    },
    phoneNumber: {
      type: String,
      required: true,
    },
    emailAddress: {
      type: String,
      required: true,
    },
    isFavorite: {
      type: Boolean,
      required: false,
      default: function() {return false;},
      // validator: function(value) {
      //   return value === '1' || value === '0';
      // }
    }
  },
  emits: ['toggle-favorite', 'delete'],

  data() {
    return {
      detailsAreVisible: false,
    };
  },

  methods: {
    toggleDetails() {
      this.detailsAreVisible = !this.detailsAreVisible;
    },
    toggleFavorite() {
      // this.friendIsFavorite = !this.friendIsFavorite;
      this.$emit('toggle-favorite', this.id);
    },
  }
}
</script>

<style scoped>

</style>
