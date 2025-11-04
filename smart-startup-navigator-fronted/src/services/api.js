import axios from 'axios';

const BASE = 'http://localhost:8456/api'; // 后端上下文路径为 /api

export const api = axios.create({
  baseURL: BASE,
  timeout: 20000,
});

// 请求拦截器，添加token
api.interceptors.request.use(
  (config) => {
    // 如果有需要，可以在这里添加token等认证信息
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器，处理错误
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      // 未登录或token过期，可以在这里处理重定向到登录页
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// 后端 Controller 实际路径映射（AIController）
// GET 同步：/ai/smart_startup/chat/sync?message=&chatId=
// GET SSE：/ai/smart_startup/chat/sse/{stagePath}?message=&chatId=
const STAGE_TO_SSE_PATH = {
  'creative': 'ecreat_stage',
  'type-definition': 'entrepreneurship_types',
  'business-plan': 'business_plan',
  'brand-copy': 'brand_and_copywriting',
  'market-analysis': 'market_analysis',
  'registration-prep': 'registration_preparation',
  'company-launch': 'company_establishment',
};

export function getSseUrl(stage, message, chatId) {
  const ssePath = STAGE_TO_SSE_PATH[stage];
  if (!ssePath) return null;
  const q = new URLSearchParams({
    message: message ?? '',
    chatId: chatId ?? '',
  }).toString();
  return `${BASE}/ai/smart_startup/chat/sse/${ssePath}?${q}`;
}

export async function sendSync(message, chatId) {
  const { data } = await api.get('/ai/smart_startup/chat/sync', {
    params: { message, chatId },
  });
  return data;
}

// 用户登录
export async function login(userAccount, userPassword) {
  const { data } = await api.post('/user/login', {
    userAccount,
    userPassword
  });
  return data;
}

// 用户注册
export async function register(userAccount, userPassword, checkPassword) {
  const { data } = await api.post('/user/register', {
    userAccount,
    userPassword,
    checkPassword
  });
  return data;
}

// 获取当前登录用户信息
export async function getCurrentUser() {
  const { data } = await api.get('/user/get/login');
  return data;
}

// 检查用户是否已登录
export function isLoggedIn() {
  return localStorage.getItem('userInfo') !== null;
}

// 退出登录
export function logout() {
  localStorage.removeItem('userInfo');
}

export { STAGE_TO_SSE_PATH };