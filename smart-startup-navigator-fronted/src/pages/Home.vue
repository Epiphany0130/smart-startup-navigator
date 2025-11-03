<template>
  <div class="container">
    <h1>Smart Startup Navigator</h1>
    <p class="subtitle">智能创业导航，陪你从创意到落地</p>
    <button class="start" @click="go">开始</button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();
const go = () => router.push('/models');
</script>

<style scoped>
.container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 16px;
}
h1 { font-size: 40px; margin: 0; }
.subtitle { opacity: 0.8; }
.start {
  background: var(--accent);
  color: #0b1c12;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 18px;
  cursor: pointer;
}
.start:hover { filter: brightness(1.1); }
</style>