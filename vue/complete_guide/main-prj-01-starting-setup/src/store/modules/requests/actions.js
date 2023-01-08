
export default {
  async contactCoach(context, payload) {
    const newRequest = {
      email: payload.email,
      message: payload.message,
    };
    const response = await fetch(`https://vue-http-demo-2273e-default-rtdb.firebaseio.com/requests/${payload.coachId}.json`,{
      method: 'POST',
      body: JSON.stringify(newRequest),
    })

    const responseData = await response.json();

    if(!response.ok) {
      const error = new Error(responseData.message || 'Failed to send request');
      throw error;
    }

    newRequest.id = responseData.name;
    newRequest.cocahId = payload.coachId;

    context.commit('addRequest', newRequest);
  },
  async fetchRequests(context) {
    const coachId = context.rootGetters.userId;
    const token = context.rootGetters['auth/token'];
    const response = await fetch(`https://vue-http-demo-2273e-default-rtdb.firebaseio.com/requests/${coachId}.json?auth=${token}`);
    const responseData = await response.json();

    if(!response.ok) {
      const error = new Error(responseData.message || 'Failed to fetch request');
      throw error;
    }

    const requests = [];

    console.log(responseData);

    for(const key in responseData) {
      const request = {
        id: key,
        coachId: coachId,
        email: responseData[key].email,
        message: responseData[key].message,
      };
      requests.push(request);
    }

    context.commit('setRequests', requests);
  }
};
