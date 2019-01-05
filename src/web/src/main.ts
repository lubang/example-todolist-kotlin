import Vue from 'vue'
import App from './App.vue'

Vue.config.productionTip = false

import Vuetify from 'vuetify'
Vue.use(Vuetify)
import 'vuetify/dist/vuetify.min.css'

import 'material-design-icons-iconfont/dist/material-design-icons.css'

new Vue({
  render: (h) => h(App),
}).$mount('#app')
