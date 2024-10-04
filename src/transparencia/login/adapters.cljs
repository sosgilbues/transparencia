(ns transparencia.login.adapters
  (:require [goog.crypt.base64 :as base64]
            [camel-snake-kebab.core :as csk]))

(defn access-toke->customer
  [access-token]
  (let [{:keys [id username roles]} (-> (clojure.string/split access-token ".")
                                        second
                                        base64/decodeString
                                        js/JSON.parse
                                        (js->clj :keywordize-keys true)
                                        :customer)]
    {:id       id
     :username username
     :roles    (map csk/->kebab-case-keyword roles)}))
