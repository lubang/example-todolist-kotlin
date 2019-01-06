<template>
  <v-app id="app">
    <v-toolbar color="primary" dark fixed flat app>
      <v-toolbar-title>Todolist</v-toolbar-title>
      <v-spacer></v-spacer>
      <TodoItemWriter />
    </v-toolbar>
    <v-content>
      <v-container fluid>
        <TodoItemTable/>
      </v-container>
    </v-content>
  </v-app>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import TodoItemWriter from './components/TodoItemWriter.vue'
import TodoItemTable from './components/TodoItemTable.vue'
import axios from 'axios'

@Component({
  components: {
    TodoItemWriter,
    TodoItemTable,
  },
})
export default class App extends Vue {
}
</script>