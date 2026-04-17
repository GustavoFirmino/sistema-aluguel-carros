import { useState, useEffect } from 'react';
import { contratoService } from '../services/api';
import toast from 'react-hot-toast';
import { ScrollText, ChevronDown, ChevronUp, Landmark, Building2 } from 'lucide-react';

export default function Contratos() {
  const [contratos, setContratos] = useState([]);
  const [loading, setLoading]     = useState(true);
  const [expanded, setExpanded]   = useState(null);

  useEffect(() => {
    contratoService.listar()
      .then(r => setContratos(r.data))
      .catch(() => toast.error('Erro ao carregar contratos.'))
      .finally(() => setLoading(false));
  }, []);

  const fmt = v => v != null
    ? new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(v)
    : '—';

  const fmtDate = s => s
    ? new Date(s + 'T00:00:00').toLocaleDateString('pt-BR')
    : '—';

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h2 className="page-title">Contratos</h2>
          <p className="page-subtitle">Contratos de aluguel gerados a partir de pedidos aprovados</p>
        </div>
      </div>

      {loading ? (
        <p className="empty-state">Carregando...</p>
      ) : contratos.length === 0 ? (
        <div className="empty-state">
          <ScrollText size={40} style={{ opacity: 0.3, marginBottom: 8 }} />
          <p>Nenhum contrato gerado ainda.</p>
        </div>
      ) : (
        <div className="card-list">
          {contratos.map(c => (
            <div key={c.id} className="card contract-card">
              <div className="contract-header" onClick={() => setExpanded(prev => prev === c.id ? null : c.id)}
                style={{ cursor: 'pointer' }}>
                <div className="contract-main">
                  <span className="contract-number">
                    <ScrollText size={15} style={{ marginRight: 6 }} />
                    {c.numeroContrato}
                  </span>
                  <span className="contract-client">{c.clienteNome}</span>
                  <span className="contract-car">{c.automovelDescricao}</span>
                </div>
                <div className="contract-meta">
                  <span className="contract-total">{fmt(c.valorTotal)}</span>
                  {expanded === c.id ? <ChevronUp size={16} /> : <ChevronDown size={16} />}
                </div>
              </div>

              {expanded === c.id && (
                <div className="contract-details">
                  <div className="detail-grid">
                    <div><span>CPF do cliente</span><strong>{c.clienteCpf}</strong></div>
                    <div>
                      <span><Building2 size={12} style={{ marginRight: 3 }} />Empresa</span>
                      <strong>{c.agenteNome || '—'}</strong>
                    </div>
                    <div>
                      <span><Landmark size={12} style={{ marginRight: 3 }} />Banco</span>
                      <strong>{c.bancoAgenteNome || '—'}</strong>
                    </div>
                    <div><span>Período</span><strong>{c.diasAluguel} dia{c.diasAluguel !== 1 ? 's' : ''}</strong></div>
                    <div><span>Valor da diária</span><strong>{fmt(c.valorDiaria)}</strong></div>
                    <div><span>Valor total</span><strong>{fmt(c.valorTotal)}</strong></div>
                    <div><span>Data do contrato</span><strong>{fmtDate(c.dataContrato)}</strong></div>
                  </div>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
