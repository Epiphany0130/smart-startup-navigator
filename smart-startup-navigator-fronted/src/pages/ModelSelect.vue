<template>
  <div class="wrap">
    <div class="topbar">
      <button class="logout-btn" @click="doLogout">注销</button>
    </div>
    <header class="page-header">
      <h2>选择你的阶段与大模型</h2>
      <p class="subtitle">选择任意阶段进入对应对话页面</p>
    </header>
    <section class="journey-map">
      <div class="journey-line"></div>
      <div v-for="(item, index) in items" :key="item.key" 
           class="stage-item" 
           :class="{'stage-left': index % 2 === 0, 'stage-right': index % 2 !== 0}"
           @click="go(item.path)">
        <div class="stage-point"></div>
        <div class="stage-content">
          <h3>{{ item.label }}</h3>
          <p>{{ item.description }}</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { logout } from '../services/api.js';
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
const doLogout = () => { logout(); router.replace('/login'); };
</script>

<style scoped>
.wrap { 
  max-width: 1024px; 
  margin: 0 auto; 
  padding: 60px 20px;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.topbar {
  display: flex; justify-content: flex-end; margin-bottom: 12px;
}
.logout-btn {
  background: var(--accent); color: #0b1c12; border: none; padding: 8px 12px; border-radius: 8px; cursor: pointer;
}
.logout-btn:hover { filter: brightness(1.1); }

.page-header { 
  margin-bottom: 60px;
  text-align: center;
}

.page-header h2 {
  font-size: 2.5rem;
  margin: 0 0 16px;
  background: linear-gradient(135deg, var(--accent), #4ade80);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.subtitle {
  font-size: 1.2rem;
  opacity: 0.8;
  margin: 0;
}

.journey-map {
  position: relative;
  padding: 20px 0;
}

.journey-line {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 50%;
  width: 4px;
  background: linear-gradient(to bottom, var(--accent), #4ade80);
  transform: translateX(-50%);
}

.stage-item {
  position: relative;
  margin-bottom: 60px;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.stage-item:last-child {
  margin-bottom: 0;
}

.stage-point {
  position: absolute;
  left: 50%;
  width: 20px;
  height: 20px;
  background: var(--accent);
  border-radius: 50%;
  transform: translateX(-50%);
  z-index: 2;
  transition: all 0.3s ease;
}

.stage-item:hover .stage-point {
  transform: translateX(-50%) scale(1.3);
  box-shadow: 0 0 15px rgba(34, 197, 94, 0.6);
}

.stage-content {
  width: 42%;
  background: var(--panel);
  border: 1px solid rgba(31, 41, 55, 0.5);
  border-radius: 16px;
  padding: 24px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.stage-item:hover .stage-content {
  transform: translateY(-5px);
  border-color: var(--accent);
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.2);
}

.stage-left .stage-content {
  margin-right: auto;
}

.stage-right .stage-content {
  margin-left: auto;
}

h3 { 
  margin: 0 0 12px; 
  font-size: 1.4rem;
}

p { 
  margin: 0; 
  opacity: 0.8; 
  line-height: 1.6;
}

@media (max-width: 768px) {
  .journey-line {
    left: 30px;
  }
  
  .stage-point {
    left: 30px;
  }
  
  .stage-content {
    width: calc(100% - 60px);
    margin-left: 60px !important;
  }
  
  .stage-left, .stage-right {
    justify-content: flex-start;
  }
}

@media (max-width: 480px) {
  .page-header h2 {
    font-size: 1.8rem;
  }
  
  .wrap {
    padding: 40px 16px;
  }
}
</style>