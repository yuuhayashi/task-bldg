package osm.surveyor.task.city.model;

public enum Status {
    PREPARATION, 	// preparation 準備中
    ACCEPTING,		// Accepting 受付中
    RESERVED,		// 予約済み
    IMPORTED,		// インポート済み
    OK,				// 検証済み
    NG,				// 不良
    END				// 完了
}
