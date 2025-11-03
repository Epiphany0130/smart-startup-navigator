import { createRouter, createWebHistory } from 'vue-router';
import Home from '../pages/Home.vue';
import ModelSelect from '../pages/ModelSelect.vue';
import Chat from '../pages/Chat.vue';

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/models', name: 'ModelSelect', component: ModelSelect },
  // Seven specific routes (each considered a distinct page)
  { path: '/chat/creative', name: 'Creative', component: Chat, props: { stage: 'creative' }, meta: { title: '创意阶段' } },
  { path: '/chat/type-definition', name: 'TypeDefinition', component: Chat, props: { stage: 'type-definition' }, meta: { title: '创业类型定义' } },
  { path: '/chat/business-plan', name: 'BusinessPlan', component: Chat, props: { stage: 'business-plan' }, meta: { title: '商业计划书' } },
  { path: '/chat/brand-copy', name: 'BrandCopy', component: Chat, props: { stage: 'brand-copy' }, meta: { title: '品牌与文案' } },
  { path: '/chat/market-analysis', name: 'MarketAnalysis', component: Chat, props: { stage: 'market-analysis' }, meta: { title: '市场分析' } },
  { path: '/chat/registration-prep', name: 'RegistrationPrep', component: Chat, props: { stage: 'registration-prep' }, meta: { title: '注册筹备' } },
  { path: '/chat/company-launch', name: 'CompanyLaunch', component: Chat, props: { stage: 'company-launch' }, meta: { title: '公司落地' } },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.afterEach((to) => {
  if (to.meta?.title) {
    document.title = `${to.meta.title} · Smart Startup Navigator`;
  } else {
    document.title = 'Smart Startup Navigator';
  }
});

export default router;