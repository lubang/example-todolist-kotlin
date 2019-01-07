import Vue from 'vue'
import App from './App.vue'
import store from './store/index'

Vue.config.productionTip = false

import Vuetify from 'vuetify'
Vue.use(Vuetify)
import 'vuetify/dist/vuetify.min.css'

import 'material-design-icons-iconfont/dist/material-design-icons.css'

import moment from 'moment'
moment.locale('ko')

new Vue({
  store,
  render: (h) => h(App),
}).$mount('#app')
