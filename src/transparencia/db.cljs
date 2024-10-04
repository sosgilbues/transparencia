(ns transparencia.db)

(def default-db
  {:access-token (.getItem (.-localStorage js/window) "access-token")})
