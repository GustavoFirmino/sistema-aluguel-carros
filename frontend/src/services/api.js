import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
});

// ── Clientes ──────────────────────────────────────────────
export const clienteService = {
  listar:        ()           => api.get('/clientes'),
  buscarPorId:   (id)         => api.get(`/clientes/${id}`),
  criar:         (data)       => api.post('/clientes', data),
  atualizar:     (id, data)   => api.put(`/clientes/${id}`, data),
  deletar:       (id)         => api.delete(`/clientes/${id}`),
};

// ── Automóveis ────────────────────────────────────────────
export const automovelService = {
  listar:          (apenasDisponiveis = false) =>
    api.get('/automoveis', { params: apenasDisponiveis ? { disponivel: true } : {} }),
  buscarPorId:     (id)       => api.get(`/automoveis/${id}`),
  criar:           (data)     => api.post('/automoveis', data),
  atualizar:       (id, data) => api.put(`/automoveis/${id}`, data),
  deletar:         (id)       => api.delete(`/automoveis/${id}`),
};

// ── Pedidos ───────────────────────────────────────────────
export const pedidoService = {
  listar:          (clienteId)    =>
    api.get('/pedidos', { params: clienteId ? { clienteId } : {} }),
  buscarPorId:     (id)           => api.get(`/pedidos/${id}`),
  criar:           (data)         => api.post('/pedidos', data),
  atualizarStatus: (id, data)     => api.patch(`/pedidos/${id}/status`, data),
  cancelar:        (id)           => api.patch(`/pedidos/${id}/cancelar`),
};

// ── Agentes ───────────────────────────────────────────────
export const agenteService = {
  listar:      (tipo)         => api.get('/agentes', { params: tipo ? { tipo } : {} }),
  buscarPorId: (id)           => api.get(`/agentes/${id}`),
  criar:       (data)         => api.post('/agentes', data),
  atualizar:   (id, data)     => api.put(`/agentes/${id}`, data),
  deletar:     (id)           => api.delete(`/agentes/${id}`),
};

// ── Contratos ─────────────────────────────────────────────
export const contratoService = {
  listar:          ()         => api.get('/contratos'),
  buscarPorPedido: (pedidoId) => api.get(`/contratos/pedido/${pedidoId}`),
  gerar:           (pedidoId) => api.post(`/contratos/pedido/${pedidoId}/gerar`),
  associarCredito: (id, data) => api.patch(`/contratos/${id}/credito`, data),
};

export default api;
