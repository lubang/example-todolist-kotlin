import Vue from 'vue'
import Vuex, { StoreOptions } from 'vuex'
import { TodoState } from './store-types'
import { actions } from '@/store/actions'
import { mutations } from '@/store/mutations'
import { getters } from '@/store/getters'

Vue.use(Vuex)

const store: StoreOptions<TodoState> = {
  state: {
    todolist: [],
    offset: 0,
    rowsPerPage: 10,
    totalCount: 0,
    error: null,
    warn: null,
  },
  actions,
  mutations,
  getters,
}

export default new Vuex.Store<TodoState>(store)
