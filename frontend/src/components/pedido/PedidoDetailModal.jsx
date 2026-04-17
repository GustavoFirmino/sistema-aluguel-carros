import { useState } from 'react';
import { X, XCircle, ScrollText } from 'lucide-react';
import toast from 'react-hot-toast';
import { pedidoService, contratoService } from '../../services/api';
import StatusBadge from '../StatusBadge';
import ConfirmModal from '../ConfirmModal';
import { useAuth, hasRole } from '../../hooks/useAuth';

export default function PedidoDetailModal({ pedido, onClose, onRefresh }) {
  const user = useAuth();
  const podeAdmin        = hasRole(user, 'admin', 'agente');
  const podeCancelar     = hasRole(user, 'admin', 'cliente');
  const isEmpresa        = user?.role === 'empresa';

  const [cancelLoading, setCancelLoading] = useState(false);
  const [gerarLoading,  setGerarLoading]  = useState(false);
  const [confirmCancel, setConfirmCancel] = useState(false);

  const handleCancel = async () => {
    setCancelLoading(true);
    try {
      await pedidoService.cancelar(pedido.id);
      toast.success('Pedido cancelado.');
      setConfirmCancel(false);
      onRefresh();
      onClose();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao cancelar.');
    } finally {
      setCancelLoading(false);
    }
  };

  const handleGerarContrato = async () => {
    if (!user?.agenteId) { toast.error('Empresa não identificada.'); return; }
    setGerarLoading(true);
    try {
      await contratoService.gerar(pedido.id, user.agenteId);
      toast.success('Contrato gerado! O pedido foi concluído.');
      onRefresh();
      onClose();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao gerar contrato.');
    } finally {
      setGerarLoading(false);
    }
  };

  const fmt   = d => d ? new Date(d + 'T00:00:00').toLocaleDateString('pt-BR') : '—';
  const fmtDT = d => d ? new Date(d).toLocaleString('pt-BR') : '—';

  const fields = [
    ['Cliente',       pedido.clienteNome],
    ['CPF',           pedido.clienteCpf],
    ['Automóvel',     `${pedido.automovelMarca} ${pedido.automovelModelo}`],
    ['Placa',         pedido.automovelPlaca],
    ['Banco',         pedido.bancoNome || '—'],
    ['Empresa',       pedido.empresaNome || '—'],
    ['Data Início',   fmt(pedido.dataInicio)],
    ['Data Fim',      fmt(pedido.dataFim)],
    ['Criado em',     fmtDT(pedido.dataCriacao)],
    ['Atualizado em', fmtDT(pedido.dataAtualizacao)],
  ];

  const podeCancelarAgora = podeCancelar && pedido.status === 'PENDENTE';
  const podeGerarContrato = (podeAdmin || isEmpresa) && pedido.status === 'APROVADO_BANCO';

  return (
    <>
      <div className="modal-backdrop" onClick={onClose}>
        <div className="modal modal-lg" onClick={e => e.stopPropagation()}>
          <div className="modal-header">
            <h2>Pedido #{pedido.id}</h2>
            <div style={{ display: 'flex', gap: '.5rem', alignItems: 'center' }}>
              <StatusBadge status={pedido.status} />
              <button className="btn-icon" onClick={onClose}><X size={16} /></button>
            </div>
          </div>
          <div className="modal-body">
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '.75rem 1.5rem', marginBottom: '1.25rem' }}>
              {fields.map(([label, value]) => (
                <div key={label}>
                  <div style={{ fontSize: '.72rem', fontWeight: 600, color: 'var(--gray-400)', textTransform: 'uppercase', letterSpacing: '.05em', marginBottom: '.2rem' }}>{label}</div>
                  <div style={{ fontSize: '.9rem', color: 'var(--gray-800)', fontWeight: 500 }}>{value}</div>
                </div>
              ))}
              {pedido.observacao && (
                <div style={{ gridColumn: 'span 2' }}>
                  <div style={{ fontSize: '.72rem', fontWeight: 600, color: 'var(--gray-400)', textTransform: 'uppercase', letterSpacing: '.05em', marginBottom: '.2rem' }}>Observação</div>
                  <div style={{ fontSize: '.9rem', color: 'var(--gray-700)' }}>{pedido.observacao}</div>
                </div>
              )}
            </div>

            <hr style={{ border: 'none', borderTop: '1px solid var(--gray-100)', margin: '1rem 0' }} />

            {podeGerarContrato && (
              <div style={{ marginBottom: '1rem', padding: '1rem', background: '#f0fdf4', borderRadius: 8, border: '1px solid #86efac' }}>
                <p style={{ fontWeight: 600, color: '#166534', marginBottom: '.5rem' }}>
                  Pedido aprovado pelo banco — pronto para fechar contrato
                </p>
                <button className="btn btn-primary" onClick={handleGerarContrato} disabled={gerarLoading}>
                  <ScrollText size={15} />
                  {gerarLoading ? 'Gerando...' : 'Gerar Contrato'}
                </button>
              </div>
            )}

            {podeCancelarAgora && (
              <div className="form-actions" style={{ borderTop: 'none' }}>
                <button className="btn btn-danger" onClick={() => setConfirmCancel(true)}>
                  <XCircle size={15} /> Cancelar Pedido
                </button>
              </div>
            )}

            {!podeGerarContrato && !podeCancelarAgora && (
              <div className="alert alert-info">
                Este pedido está em um estado que não permite ações adicionais aqui.
              </div>
            )}
          </div>
        </div>
      </div>

      {confirmCancel && (
        <ConfirmModal
          title="Cancelar Pedido"
          message={`Tem certeza que deseja cancelar o pedido #${pedido.id}? O automóvel voltará a ficar disponível.`}
          onConfirm={handleCancel}
          onCancel={() => setConfirmCancel(false)}
          loading={cancelLoading}
        />
      )}
    </>
  );
}
