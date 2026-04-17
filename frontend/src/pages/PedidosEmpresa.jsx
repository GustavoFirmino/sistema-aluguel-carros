import { useEffect, useState } from 'react';
import { Building2, FileText, ScrollText, XCircle } from 'lucide-react';
import toast from 'react-hot-toast';
import { pedidoService, contratoService } from '../services/api';
import StatusBadge from '../components/StatusBadge';

const fmt = d => d ? new Date(d + 'T00:00:00').toLocaleDateString('pt-BR') : '—';
const fmtR$ = v => v != null ? new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(v) : '—';

export default function PedidosEmpresa({ usuario }) {
  const empresaId = usuario?.agenteId;
  const [pedidos,  setPedidos]  = useState([]);
  const [loading,  setLoading]  = useState(true);
  const [obs,      setObs]      = useState({});
  const [acting,   setActing]   = useState(null);

  const load = () => {
    setLoading(true);
    pedidoService.listarParaEmpresa()
      .then(r => setPedidos(Array.isArray(r.data) ? r.data : (r.data.items ?? [])))
      .catch(() => toast.error('Erro ao carregar pedidos.'))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const fecharContrato = async (pedidoId) => {
    if (!empresaId) { toast.error('Empresa não identificada.'); return; }
    setActing(pedidoId + 'contrato');
    try {
      await contratoService.gerar(pedidoId, empresaId);
      toast.success('Contrato gerado e pedido concluído!');
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao gerar contrato.');
    } finally {
      setActing(null);
    }
  };

  const rejeitar = async (pedidoId) => {
    setActing(pedidoId + 'rejeitar');
    try {
      await pedidoService.atualizarStatus(pedidoId, { status: 'REJEITADO', observacao: obs[pedidoId] || 'Rejeitado pela empresa.' });
      toast.success('Pedido rejeitado.');
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao rejeitar.');
    } finally {
      setActing(null);
    }
  };

  return (
    <div className="page">
      <div style={{ marginBottom: '1.5rem' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '.6rem', marginBottom: '.25rem' }}>
          <Building2 size={22} color="#7c3aed" />
          <h2 style={{ fontSize: '1.25rem', fontWeight: 700, color: '#111827' }}>Pedidos para Contrato</h2>
        </div>
        <p style={{ color: '#6b7280', fontSize: '.875rem' }}>
          Pedidos aprovados pelo banco — feche o contrato para concluir o aluguel
        </p>
      </div>

      <div className="panel">
        <div className="panel-header">
          <span className="panel-title">{pedidos.length} pedido{pedidos.length !== 1 ? 's' : ''} aprovado{pedidos.length !== 1 ? 's' : ''} pelo banco</span>
        </div>

        {loading ? (
          <div className="loading-center"><div className="spinner" /></div>
        ) : pedidos.length === 0 ? (
          <div className="empty-state"><FileText size={36} /><p>Nenhum pedido aguardando sua aprovação.</p></div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>#</th><th>Cliente</th><th>Veículo</th><th>Banco</th>
                  <th>Período</th><th>Valor Total</th><th>Status</th><th>Obs.</th><th>Ações</th>
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
                      <td style={{ fontWeight: 500 }}>{p.clienteNome}
                        <div style={{ fontSize: '.75rem', color: '#9ca3af' }}>{p.clienteCpf}</div>
                      </td>
                      <td>{p.automovelMarca} {p.automovelModelo}
                        <div style={{ fontSize: '.75rem', color: '#9ca3af' }}>{p.automovelPlaca}</div>
                      </td>
                      <td style={{ fontSize: '.82rem', color: '#2563eb', fontWeight: 500 }}>
                        {p.bancoNome || '—'}
                      </td>
                      <td style={{ fontSize: '.82rem' }}>
                        {fmt(p.dataInicio)} → {fmt(p.dataFim)}
                        {dias && <div style={{ color: '#6b7280' }}>{dias} dia{dias !== 1 ? 's' : ''}</div>}
                      </td>
                      <td style={{ fontWeight: 700, color: '#059669' }}>
                        {dias && p.valorDiaria ? fmtR$(p.valorDiaria * dias) : '—'}
                      </td>
                      <td><StatusBadge status={p.status} /></td>
                      <td>
                        <input
                          className="form-control"
                          style={{ minWidth: 140, fontSize: '.8rem' }}
                          placeholder="Observação"
                          value={obs[p.id] || ''}
                          onChange={e => setObs(o => ({ ...o, [p.id]: e.target.value }))}
                        />
                      </td>
                      <td>
                        <div className="td-actions">
                          <button
                            className="btn btn-primary btn-sm"
                            disabled={acting === p.id + 'contrato'}
                            onClick={() => fecharContrato(p.id)}
                            title="Fechar contrato"
                          >
                            <ScrollText size={14} /> Fechar Contrato
                          </button>
                          <button
                            className="btn btn-ghost btn-sm danger"
                            disabled={acting === p.id + 'rejeitar'}
                            onClick={() => rejeitar(p.id)}
                            title="Rejeitar pedido"
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
