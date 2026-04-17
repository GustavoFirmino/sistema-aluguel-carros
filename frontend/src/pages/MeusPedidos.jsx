import { useEffect, useState } from 'react';
import { FileText, Eye, Clock, CheckCircle, XCircle } from 'lucide-react';
import toast from 'react-hot-toast';
import { pedidoService } from '../services/api';
import StatusBadge from '../components/StatusBadge';
import PedidoDetailModal from '../components/pedido/PedidoDetailModal';

const fmt = (d) => d ? new Date(d + 'T00:00:00').toLocaleDateString('pt-BR') : '—';

const RESUMO = [
  { label: 'Aguard. Banco', icon: Clock,        key: 'PENDENTE',       color: '#d97706', bg: '#fffbeb' },
  { label: 'Aprov. Banco',  icon: CheckCircle,  key: 'APROVADO_BANCO', color: '#2563eb', bg: '#eff6ff' },
  { label: 'Concluídos',    icon: CheckCircle,  key: 'CONCLUIDO',      color: '#16a34a', bg: '#f0fdf4' },
  { label: 'Cancelados',    icon: XCircle,      key: 'CANCELADO',      color: '#dc2626', bg: '#fef2f2' },
];

export default function MeusPedidos({ usuario }) {
  const [pedidos, setPedidos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [detalhe, setDetalhe] = useState(null);
  const [filtro, setFiltro] = useState('');

  const load = () => {
    if (!usuario?.clienteId) { setLoading(false); return; }
    setLoading(true);
    pedidoService.listar(usuario.clienteId)
      .then(r => setPedidos(Array.isArray(r.data) ? r.data : (r.data.items ?? [])))
      .catch(() => toast.error('Erro ao carregar pedidos.'))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const filtered = filtro ? pedidos.filter(p => p.status === filtro) : pedidos;

  const conta = (status) => pedidos.filter(p => p.status === status).length;

  return (
    <div style={{ maxWidth: 900, margin: '0 auto', padding: '2rem 1.5rem' }}>
      <div style={{ marginBottom: '1.75rem' }}>
        <h2 style={{ fontSize: '1.4rem', fontWeight: 800, color: '#111827' }}>Meus Pedidos</h2>
        <p style={{ color: '#6b7280', marginTop: '.25rem', fontSize: '.9rem' }}>
          Acompanhe o status dos seus aluguéis
        </p>
      </div>

      {/* Cards de resumo */}
      {!loading && (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(140px, 1fr))', gap: '1rem', marginBottom: '1.75rem' }}>
          {RESUMO.map(({ label, icon: Icon, key, color, bg }) => (
            <div key={key} style={{
              background: bg, borderRadius: 10, border: `1px solid ${color}22`,
              padding: '1rem 1.1rem', cursor: 'pointer',
              outline: filtro === key ? `2px solid ${color}` : 'none',
              transition: 'transform .15s',
            }}
              onClick={() => setFiltro(filtro === key ? '' : key)}
              onMouseEnter={e => e.currentTarget.style.transform = 'translateY(-2px)'}
              onMouseLeave={e => e.currentTarget.style.transform = 'translateY(0)'}
            >
              <Icon size={20} color={color} />
              <div style={{ fontSize: '1.6rem', fontWeight: 800, color, lineHeight: 1.2, marginTop: '.3rem' }}>
                {conta(key)}
              </div>
              <div style={{ fontSize: '.78rem', fontWeight: 600, color, opacity: .8 }}>{label}</div>
            </div>
          ))}
        </div>
      )}

      {/* Tabela */}
      <div style={{
        background: '#fff', borderRadius: 12, border: '1px solid #e5e7eb',
        boxShadow: '0 2px 8px rgba(0,0,0,.05)', overflow: 'hidden',
      }}>
        <div style={{
          padding: '.85rem 1.25rem', borderBottom: '1px solid #f3f4f6',
          display: 'flex', alignItems: 'center', justifyContent: 'space-between',
        }}>
          <span style={{ fontWeight: 600, fontSize: '.9rem', color: '#374151' }}>
            {filtered.length} pedido{filtered.length !== 1 ? 's' : ''}
            {filtro && <span style={{ color: '#9ca3af', fontWeight: 400 }}> com filtro ativo</span>}
          </span>
          {filtro && (
            <button onClick={() => setFiltro('')}
              style={{ fontSize: '.78rem', color: '#6b7280', background: 'none', border: 'none', cursor: 'pointer', textDecoration: 'underline' }}>
              limpar filtro
            </button>
          )}
        </div>

        {loading ? (
          <div style={{ display: 'flex', justifyContent: 'center', padding: '3rem' }}>
            <div className="spinner" />
          </div>
        ) : filtered.length === 0 ? (
          <div className="empty-state">
            <FileText size={36} />
            <p>{pedidos.length === 0 ? 'Você ainda não fez nenhum pedido.' : 'Nenhum pedido com esse status.'}</p>
          </div>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '.855rem' }}>
              <thead>
                <tr style={{ background: '#f9fafb', borderBottom: '1px solid #e5e7eb' }}>
                  {['#', 'Veículo', 'Placa', 'Início', 'Fim', 'Status', ''].map(h => (
                    <th key={h} style={{ padding: '.6rem 1rem', textAlign: 'left', fontSize: '.72rem', fontWeight: 600, textTransform: 'uppercase', letterSpacing: '.06em', color: '#6b7280', whiteSpace: 'nowrap' }}>
                      {h}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {filtered.map(p => (
                  <tr key={p.id} style={{ borderBottom: '1px solid #f3f4f6' }}
                    onMouseEnter={e => e.currentTarget.style.background = '#f9fafb'}
                    onMouseLeave={e => e.currentTarget.style.background = 'transparent'}>
                    <td style={{ padding: '.75rem 1rem', color: '#9ca3af', fontSize: '.8rem' }}>{p.id}</td>
                    <td style={{ padding: '.75rem 1rem', fontWeight: 500, color: '#111827' }}>
                      {p.automovelMarca} {p.automovelModelo}
                    </td>
                    <td style={{ padding: '.75rem 1rem' }}>
                      <span style={{ fontFamily: 'monospace', background: '#f3f4f6', padding: '.1rem .4rem', borderRadius: 4, fontSize: '.8rem' }}>
                        {p.automovelPlaca}
                      </span>
                    </td>
                    <td style={{ padding: '.75rem 1rem', color: '#374151' }}>{fmt(p.dataInicio)}</td>
                    <td style={{ padding: '.75rem 1rem', color: '#374151' }}>{fmt(p.dataFim)}</td>
                    <td style={{ padding: '.75rem 1rem' }}><StatusBadge status={p.status} /></td>
                    <td style={{ padding: '.75rem 1rem' }}>
                      <button onClick={() => setDetalhe(p)}
                        style={{ padding: '.35rem .55rem', borderRadius: 6, border: '1px solid #e5e7eb', background: '#fff', cursor: 'pointer', display: 'flex', alignItems: 'center', color: '#6b7280' }}>
                        <Eye size={14} />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

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
