import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
});

// ── Interceptor de requisição: injeta o token de autenticação ──
api.interceptors.request.use(config => {
  const token = localStorage.getItem('auth_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// ── Interceptor de resposta: trata erros globais ──────────────
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      // Sessão expirada ou token inválido — força logout
      localStorage.removeItem('auth_token');
      localStorage.removeItem('auth_user');
      window.location.reload();
    }
    return Promise.reject(error);
  }
);

// ── Autenticação ──────────────────────────────────────────────
export const authService = {
  login:    (data) => api.post('/auth/login', data),
  logout:   ()     => api.post('/auth/logout'),
  cadastro: (data) => api.post('/auth/cadastro', data),
};

// ── Clientes ──────────────────────────────────────────────────
export const clienteService = {
  listar:      (pagina = 0, tamanho = 50) =>
    api.get('/clientes', { params: { pagina, tamanho } }),
  buscarPorId: (id)         => api.get(`/clientes/${id}`),
  criar:       (data)       => api.post('/clientes', data),
  atualizar:   (id, data)   => api.put(`/clientes/${id}`, data),
  deletar:     (id)         => api.delete(`/clientes/${id}`),
};

// ── Automóveis ────────────────────────────────────────────────
export const automovelService = {
  listar:      (apenasDisponiveis = false, pagina = 0, tamanho = 50) =>
    api.get('/automoveis', { params: { pagina, tamanho, ...(apenasDisponiveis ? { disponivel: true } : {}) } }),
  buscarPorId: (id)       => api.get(`/automoveis/${id}`),
  criar:       (data)     => api.post('/automoveis', data),
  atualizar:   (id, data) => api.put(`/automoveis/${id}`, data),
  deletar:     (id)       => api.delete(`/automoveis/${id}`),
};

// ── Pedidos ───────────────────────────────────────────────────
export const pedidoService = {
  listar:            (clienteId, pagina = 0, tamanho = 50) =>
    api.get('/pedidos', { params: { pagina, tamanho, ...(clienteId ? { clienteId } : {}) } }),
  listarPorBanco:    (bancoId) =>
    api.get('/pedidos', { params: { bancoId } }),
  listarParaEmpresa: () =>
    api.get('/pedidos', { params: { paraEmpresa: true } }),
  buscarPorId:       (id)           => api.get(`/pedidos/${id}`),
  criar:             (data)         => api.post('/pedidos', data),
  atualizarStatus:   (id, data)     => api.patch(`/pedidos/${id}/status`, data),
  cancelar:          (id)           => api.patch(`/pedidos/${id}/cancelar`),
};

// ── Agentes ───────────────────────────────────────────────────
export const agenteService = {
  listar:      (tipo)         => api.get('/agentes', { params: tipo ? { tipo } : {} }),
  buscarPorId: (id)           => api.get(`/agentes/${id}`),
  criar:       (data)         => api.post('/agentes', data),
  atualizar:   (id, data)     => api.put(`/agentes/${id}`, data),
  deletar:     (id)           => api.delete(`/agentes/${id}`),
};

// ── Contratos ─────────────────────────────────────────────────
export const contratoService = {
  listar:          ()                    => api.get('/contratos'),
  buscarPorPedido: (pedidoId)            => api.get(`/contratos/pedido/${pedidoId}`),
  gerar:           (pedidoId, empresaId) => api.post(`/contratos/pedido/${pedidoId}/gerar`, null, { params: { empresaId } }),
};

export default api;
