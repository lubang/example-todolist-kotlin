<template>
  <v-dialog v-model="dialog" width="500">
    <v-btn slot="activator" class="light-blue">새 할 일 추가
      <v-icon right>done</v-icon>
    </v-btn>
    <v-card>
      <v-card-title class="headline grey lighten-2" primary-title>할 일 추가하기</v-card-title>
      <v-card-text>
        <v-text-field v-model="message"></v-text-field>
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" flat @click="addTodoItem()">추가</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import axios from 'axios'

@Component
export default class TodoItemEditor extends Vue {
  private dialog = false
  private message = ''

  private addTodoItem() {
    axios
      .post('/api/todo-items', { message: this.message })
      .then((response) => {
        console.log(response)
      })
      .catch((error) => {
        console.log(error)
      })
    this.dialog = false
    this.message = ''
  }
}
</script>
