import { useEffect, useState } from 'react';
import { Plus, Eye, FileText } from 'lucide-react';
import toast from 'react-hot-toast';
import { pedidoService } from '../services/api';
import StatusBadge from '../components/StatusBadge';
import NovoPedidoModal from '../components/pedido/NovoPedidoModal';
import PedidoDetailModal from '../components/pedido/PedidoDetailModal';
import { useAuth, hasRole } from '../hooks/useAuth';

const ALL_STATUS = ['PENDENTE', 'APROVADO_BANCO', 'REJEITADO', 'CANCELADO', 'CONCLUIDO'];

const STATUS_LABELS = {
  PENDENTE: 'Pendente', APROVADO_BANCO: 'Aprovado Banco',
  REJEITADO: 'Rejeitado', CANCELADO: 'Cancelado', CONCLUIDO: 'Concluído',
};

export default function Pedidos() {
  const user = useAuth();
  const [pedidos, setPedidos]       = useState([]);
  const [loading, setLoading]       = useState(true);
  const [filtroStatus, setFiltroStatus] = useState('');
  const [novoPedido, setNovoPedido] = useState(false);
  const [detalhe, setDetalhe]       = useState(null);

  const load = () => {
    setLoading(true);
    pedidoService.listar()
      .then(r => setPedidos(r.data.items ?? (Array.isArray(r.data) ? r.data : [])))
      .catch(() => toast.error('Erro ao carregar pedidos.'))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const filtered = filtroStatus
    ? pedidos.filter(p => p.status === filtroStatus)
    : pedidos;

  const fmt = d => d ? new Date(d + 'T00:00:00').toLocaleDateString('pt-BR') : '—';

  return (
    <div className="page">
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1.5rem' }}>
        <div>
          <h2 style={{ fontSize: '1.25rem', fontWeight: 700, color: 'var(--gray-900)' }}>Pedidos de Aluguel</h2>
          <p style={{ color: 'var(--gray-500)', fontSize: '.875rem' }}>Crie e acompanhe o status dos pedidos</p>
        </div>
        {hasRole(user, 'admin', 'cliente') && (
          <button className="btn btn-primary" onClick={() => setNovoPedido(true)}>
            <Plus size={16} /> Novo Pedido
          </button>
        )}
      </div>

      <div className="panel">
        <div className="panel-header">
          <span className="panel-title">
            {filtered.length} pedido{filtered.length !== 1 ? 's' : ''}
          </span>
          <select
            className="form-control"
            style={{ width: 'auto', minWidth: 160 }}
            value={filtroStatus}
            onChange={e => setFiltroStatus(e.target.value)}
          >
            <option value="">Todos os status</option>
            {ALL_STATUS.map(s => <option key={s} value={s}>{STATUS_LABELS[s]}</option>)}
          </select>
        </div>

        {loading ? (
          <div className="loading-center"><div className="spinner" /></div>
        ) : filtered.length === 0 ? (
          <div className="empty-state">
            <FileText size={36} />
            <p>{filtroStatus ? 'Nenhum pedido com este status.' : 'Nenhum pedido registrado ainda.'}</p>
          </div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Cliente</th>
                  <th>Automóvel</th>
                  <th>Placa</th>
                  <th>Início</th>
                  <th>Fim</th>
                  <th>Status</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map(p => (
                  <tr key={p.id}>
                    <td style={{ color: 'var(--gray-400)', fontSize: '.8rem' }}>{p.id}</td>
                    <td style={{ fontWeight: 500 }}>{p.clienteNome}</td>
                    <td>{p.automovelMarca} {p.automovelModelo}</td>
                    <td>
                      <span style={{ fontFamily: 'monospace', background: 'var(--gray-100)', padding: '.1rem .4rem', borderRadius: 4, fontSize: '.8rem' }}>
                        {p.automovelPlaca}
                      </span>
                    </td>
                    <td>{fmt(p.dataInicio)}</td>
                    <td>{fmt(p.dataFim)}</td>
                    <td><StatusBadge status={p.status} /></td>
                    <td>
                      <button className="btn-icon" title="Ver detalhes" onClick={() => setDetalhe(p)}>
                        <Eye size={15} />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {novoPedido && (
        <NovoPedidoModal
          onClose={() => setNovoPedido(false)}
          onSave={() => { setNovoPedido(false); load(); }}
        />
      )}

      {detalhe && (
        <PedidoDetailModal
          pedido={detalhe}
          onClose={() => setDetalhe(null)}
          onRefresh={load}
        />
      )}
    </div>
  );
}
