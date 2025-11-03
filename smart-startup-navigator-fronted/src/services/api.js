import axios from 'axios';

const BASE = 'http://localhost:8456/api'; // 后端上下文路径为 /api

export const api = axios.create({
  baseURL: BASE,
  timeout: 20000,
});

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

export { STAGE_TO_SSE_PATH };