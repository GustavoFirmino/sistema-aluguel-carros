import { useEffect, useState } from 'react';
import { CheckCircle, XCircle, Landmark, FileText } from 'lucide-react';
import toast from 'react-hot-toast';
import { pedidoService } from '../services/api';
import StatusBadge from '../components/StatusBadge';

const fmt = d => d ? new Date(d + 'T00:00:00').toLocaleDateString('pt-BR') : '—';
const fmtR$ = v => v != null ? new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(v) : '—';

export default function PedidosBanco({ usuario }) {
  const bancoId = usuario?.agenteId;
  const [pedidos, setPedidos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [obs, setObs]         = useState({});
  const [acting, setActing]   = useState(null);

  const load = () => {
    if (!bancoId) return;
    setLoading(true);
    pedidoService.listarPorBanco(bancoId)
      .then(r => setPedidos(Array.isArray(r.data) ? r.data : (r.data.items ?? [])))
      .catch(() => toast.error('Erro ao carregar pedidos.'))
      .finally(() => setLoading(false));
  };

  useEffect(load, [bancoId]);

  const agir = async (id, status) => {
    setActing(id + status);
    try {
      await pedidoService.atualizarStatus(id, { status, observacao: obs[id] || '' });
      toast.success(status === 'APROVADO_BANCO' ? 'Pedido aprovado!' : 'Pedido rejeitado.');
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao atualizar.');
    } finally {
      setActing(null);
    }
  };

  return (
    <div className="page">
      <div style={{ marginBottom: '1.5rem' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '.6rem', marginBottom: '.25rem' }}>
          <Landmark size={22} color="#3b82f6" />
          <h2 style={{ fontSize: '1.25rem', fontWeight: 700, color: '#111827' }}>Análise de Crédito</h2>
        </div>
        <p style={{ color: '#6b7280', fontSize: '.875rem' }}>
          Pedidos pendentes dos clientes vinculados ao seu banco
        </p>
      </div>

      <div className="panel">
        <div className="panel-header">
          <span className="panel-title">{pedidos.length} pedido{pedidos.length !== 1 ? 's' : ''} aguardando análise</span>
        </div>

        {loading ? (
          <div className="loading-center"><div className="spinner" /></div>
        ) : pedidos.length === 0 ? (
          <div className="empty-state"><FileText size={36} /><p>Nenhum pedido pendente para seu banco.</p></div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>#</th><th>Cliente</th><th>CPF</th><th>Veículo</th>
                  <th>Período</th><th>Valor</th><th>Status</th><th>Observação</th><th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {pedidos.map(p => {
                  const dias = p.dataInicio && p.dataFim
                    ? Math.max(1, Math.ceil((new Date(p.dataFim) - new Date(p.dataInicio)) / 86400000))
                    : null;
                  return (
                    <tr key={p.id}>
                      <td style={{ color: '#9ca3af', fontSize: '.8rem' }}>{p.id}</td>
                      <td style={{ fontWeight: 500 }}>{p.clienteNome}</td>
                      <td style={{ fontFamily: 'monospace', fontSize: '.82rem' }}>{p.clienteCpf}</td>
                      <td>{p.automovelMarca} {p.automovelModelo}
                        <div style={{ fontSize: '.75rem', color: '#9ca3af' }}>{p.automovelPlaca}</div>
                      </td>
                      <td style={{ fontSize: '.82rem' }}>
                        {fmt(p.dataInicio)} → {fmt(p.dataFim)}
                        {dias && <div style={{ color: '#6b7280' }}>{dias} dia{dias !== 1 ? 's' : ''}</div>}
                      </td>
                      <td style={{ fontWeight: 600, color: '#059669' }}>
                        {dias ? fmtR$(p.valorDiaria * dias) : '—'}
                      </td>
                      <td><StatusBadge status={p.status} /></td>
                      <td>
                        <input
                          className="form-control"
                          style={{ minWidth: 160, fontSize: '.8rem' }}
                          placeholder="Observação (opcional)"
                          value={obs[p.id] || ''}
                          onChange={e => setObs(o => ({ ...o, [p.id]: e.target.value }))}
                        />
                      </td>
                      <td>
                        <div className="td-actions">
                          <button
                            className="btn btn-primary btn-sm"
                            disabled={acting === p.id + 'APROVADO_BANCO'}
                            onClick={() => agir(p.id, 'APROVADO_BANCO')}
                            title="Aprovar crédito"
                          >
                            <CheckCircle size={14} /> Aprovar
                          </button>
                          <button
                            className="btn btn-ghost btn-sm danger"
                            disabled={acting === p.id + 'REJEITADO'}
                            onClick={() => agir(p.id, 'REJEITADO')}
                            title="Rejeitar"
                          >
                            <XCircle size={14} /> Rejeitar
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
