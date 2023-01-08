export default {
  coaches(state) {
    return state.coaches;
  },
  hasCoaches(state) {
    return state.coaches && state.coaches.length > 0;
  },
  isCoach(_, getters, _2, rootGetters) {
    const coaches = getters.coaches;
    const userId = rootGetters.userId;
    return coaches.some(coach => coach.id === userId);
  },
  shouldUpdate(state) {
    const lastFetch = state.lastFetch;
    //처음 가져올 때 null
    if (!lastFetch) {
      return true;
    }
    const currentTimeStamp = new Date().getTime();
    //1분보다 더 지났는가? ms
    return (currentTimeStamp - lastFetch) / 1000 > 60;
  }
};
