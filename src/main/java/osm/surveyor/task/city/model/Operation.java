package osm.surveyor.task.city.model;

/**
 * マッパーの操作
 * @author hayashi
 *
 */
public enum Operation {
	NOP,			// ノーオペ
    RESERVE, 		// 予約
    CANCEL,			// 取消
    DONE,			// インポート済み
    VALID,			// 検証
    OK,				// 検証済み
    NG,				// 検証不良
    END,			// 完了
    VIEW			// 参照
}
