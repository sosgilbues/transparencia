(ns transparencia.transaction.events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(re-frame/reg-event-fx
  ::fetch-transaction
  (fn-traced [{:keys [db]} [_ transaction-id]]
    {:http-xhrio {:method          :get
                  :uri             (str "https://api-ong-transparencia.sosgilbues.org.br/api/transactions/" transaction-id)
                  :timeout         5000
                  :response-format (ajax/json-response-format {:keywords? true}) ;; IMPORTANT!: You must provide this.
                  :on-success      [::fetch-one-transaction-success]
                  :on-failure      [::create-failure]}}))

(re-frame/reg-event-fx
  ::fetch-one-transaction-success
  (fn-traced [{:keys [db]} [_ {:keys [transaction]}]]
    {:db (assoc db :transaction transaction)}))
