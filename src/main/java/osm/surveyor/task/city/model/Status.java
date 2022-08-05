package osm.surveyor.task.city.model;

public enum Status {
    PREPARATION, 	// preparation 準備中
    ACCEPTING,		// Accepting 受付中
    RESERVED,		// 予約済み
    IMPORTED,		// インポート済み
    VERIFICATION,	// 検証者登録
    END				// 完了
}
