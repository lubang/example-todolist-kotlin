<template>
  <v-app id="app">
    <v-toolbar color="primary" dark fixed flat app>
      <v-toolbar-title>Todolist</v-toolbar-title>
      <span class="ml-4">
        (총
        <strong>{{ totalCount }}</strong> 건)
      </span>
      <v-spacer></v-spacer>
      <TodoItemWriter/>
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

@Component({
  components: {
    TodoItemWriter,
    TodoItemTable,
  },
})
export default class App extends Vue {
  private get totalCount(): number {
    return this.$store.getters.totalCount
  }
}
</script>