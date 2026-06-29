<template>
  <router-view />
</template>

<script setup lang="ts">
import { useUserStore } from './stores/user'
const userStore = useUserStore()
userStore.loadUserInfo()
</script>

<style>
body { margin: 0; padding: 0; }
#app { height: 100vh; }
</style>
