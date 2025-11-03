<template>
  <div class="wrap">
    <header>
      <h2>选择你的阶段与大模型</h2>
      <p>点击任意卡片进入对应对话页面</p>
    </header>
    <section class="grid">
      <div v-for="item in items" :key="item.key" class="card" @click="go(item.path)">
        <h3>{{ item.label }}</h3>
        <p>{{ item.description }}</p>
      </div>
    </section>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { STAGES } from '../constants/stages.js';
const router = useRouter();
const items = [
  { ...STAGES['creative'], path: '/chat/creative' },
  { ...STAGES['type-definition'], path: '/chat/type-definition' },
  { ...STAGES['business-plan'], path: '/chat/business-plan' },
  { ...STAGES['brand-copy'], path: '/chat/brand-copy' },
  { ...STAGES['market-analysis'], path: '/chat/market-analysis' },
  { ...STAGES['registration-prep'], path: '/chat/registration-prep' },
  { ...STAGES['company-launch'], path: '/chat/company-launch' },
];
const go = (path) => router.push(path);
</script>

<style scoped>
.wrap { max-width: 1024px; margin: 0 auto; padding: 40px 20px; }
header { margin-bottom: 16px; }
.grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
}
.card {
  background: var(--panel);
  border: 1px solid #1f2937;
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
}
.card:hover { border-color: var(--accent); }
h3 { margin: 0 0 8px; }
p { margin: 0; opacity: 0.8; }
@media (max-width: 900px) { .grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 600px) { .grid { grid-template-columns: 1fr; } }
</style>