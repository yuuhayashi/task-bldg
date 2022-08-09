package osm.surveyor.task.city.model;

public enum Status {
    PREPARATION, 	// preparation 準備中	"X"
    ACCEPTING,		// Accepting 受付中		"1"
    RESERVED,		// 予約済み				"2"
    IMPORTED,		// インポート済み		"3"
    NG,				// 検証(NG)				"0"
    END				// 完了					"4"
}
