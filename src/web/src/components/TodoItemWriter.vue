<template>
  <v-dialog v-model="dialog" width="500">
    <v-btn slot="activator" class="light-blue">새 할 일 추가
      <v-icon right>done</v-icon>
    </v-btn>
    <v-card>
      <v-card-title class="headline grey lighten-2" primary-title>할 일 추가하기</v-card-title>
      <v-card-text>
        <v-text-field v-model="message" @keypress.enter.prevent="addTodoItem()"></v-text-field>
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-btn flat @click="dialog = false">닫기</v-btn>
        <v-spacer></v-spacer>
        <v-btn color="primary" flat @click="addTodoItem()">추가</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component
export default class TodoItemEditor extends Vue {
  private dialog = false
  private message = ''

  private addTodoItem() {
    if (this.message === '') {
      return
    }

    this.$store.dispatch('addTodoItem', { message: this.message }).then(() => {
      this.message = ''
      setTimeout(() => {
        this.$store.dispatch('fetchTodolist', {})
      }, 300)
    })
  }
}
</script>
