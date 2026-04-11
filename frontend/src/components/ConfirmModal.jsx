import { AlertTriangle, X } from 'lucide-react';

export default function ConfirmModal({ title, message, onConfirm, onCancel, loading, danger = true }) {
  return (
    <div className="modal-backdrop" onClick={onCancel}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2 style={{ display: 'flex', alignItems: 'center', gap: '.5rem' }}>
            {danger && <AlertTriangle size={18} color="var(--danger)" />}
            {title}
          </h2>
          <button className="btn-icon" onClick={onCancel}><X size={16} /></button>
        </div>
        <div className="modal-body">
          <p style={{ color: 'var(--gray-600)', marginBottom: '1.5rem' }}>{message}</p>
          <div className="form-actions">
            <button className="btn btn-ghost" onClick={onCancel} disabled={loading}>Cancelar</button>
            <button
              className={`btn ${danger ? 'btn-danger' : 'btn-primary'}`}
              onClick={onConfirm}
              disabled={loading}
            >
              {loading ? 'Aguarde...' : 'Confirmar'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
