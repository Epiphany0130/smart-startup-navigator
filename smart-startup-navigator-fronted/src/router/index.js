import { createRouter, createWebHistory } from 'vue-router';
import Home from '../pages/Home.vue';
import ModelSelect from '../pages/ModelSelect.vue';
import Chat from '../pages/Chat.vue';
import Login from '../pages/Login.vue';
import Register from '../pages/Register.vue';
import { isLoggedIn } from '../services/api';

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/login', name: 'Login', component: Login, meta: { title: '登录' } },
  { path: '/register', name: 'Register', component: Register, meta: { title: '注册' } },
  { 
    path: '/models', 
    name: 'ModelSelect', 
    component: ModelSelect,
    meta: { requiresAuth: true }
  },
  // Seven specific routes (each considered a distinct page)
  { 
    path: '/chat/creative', 
    name: 'Creative', 
    component: Chat, 
    props: { stage: 'creative' }, 
    meta: { title: '创意阶段', requiresAuth: true } 
  },
  { 
    path: '/chat/type-definition', 
    name: 'TypeDefinition', 
    component: Chat, 
    props: { stage: 'type-definition' }, 
    meta: { title: '创业类型定义', requiresAuth: true } 
  },
  { 
    path: '/chat/business-plan', 
    name: 'BusinessPlan', 
    component: Chat, 
    props: { stage: 'business-plan' }, 
    meta: { title: '商业计划书', requiresAuth: true } 
  },
  { 
    path: '/chat/brand-copy', 
    name: 'BrandCopy', 
    component: Chat, 
    props: { stage: 'brand-copy' }, 
    meta: { title: '品牌与文案', requiresAuth: true } 
  },
  { 
    path: '/chat/market-analysis', 
    name: 'MarketAnalysis', 
    component: Chat, 
    props: { stage: 'market-analysis' }, 
    meta: { title: '市场分析', requiresAuth: true } 
  },
  { 
    path: '/chat/registration-prep', 
    name: 'RegistrationPrep', 
    component: Chat, 
    props: { stage: 'registration-prep' }, 
    meta: { title: '注册筹备', requiresAuth: true } 
  },
  { 
    path: '/chat/company-launch', 
    name: 'CompanyLaunch', 
    component: Chat, 
    props: { stage: 'company-launch' }, 
    meta: { title: '公司落地', requiresAuth: true } 
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 全局路由守卫，检查登录状态
router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // 需要登录的路由
    if (!isLoggedIn()) {
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      });
    } else {
      next();
    }
  } else {
    next();
  }
});

router.afterEach((to) => {
  if (to.meta?.title) {
    document.title = `${to.meta.title} · Smart Startup Navigator`;
  } else {
    document.title = 'Smart Startup Navigator';
  }
});

export default router;