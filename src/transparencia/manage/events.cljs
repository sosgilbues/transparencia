(ns transparencia.manage.events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(re-frame/reg-event-fx
  ::update-form
  (fn-traced [{:keys [db]} [_ id value]]
             {:db (assoc-in db [:new-transaction-form id] value)}))

(re-frame/reg-event-fx
  ::create-transaction
  (fn-traced [{:keys [db]} _]
             {:http-xhrio {:method          :post
                           :uri             "http://localhost:8080/api/transactions"
                           :params          {:transaction {:title          (-> db :new-transaction-form :title)
                                                           :description    (-> db :new-transaction-form :description)
                                                           :type           (-> db :new-transaction-form :type)
                                                           :amount         (-> db :new-transaction-form :amount)
                                                           :reference-date (-> db :new-transaction-form :reference-date)}}
                           :timeout         5000
                           :format          (ajax/json-request-format)
                           :response-format (ajax/json-response-format {:keywords? true}) ;; IMPORTANT!: You must provide this.
                           :on-success      [::create-success]
                           :on-failure      [::create-failure]}}))

(re-frame/reg-event-fx
  ::create-success
  (fn-traced [{:keys [db]} [_ {:keys [transaction]}]]
             {:db (update db :transactions conj transaction)}))

(re-frame/reg-event-fx
  ::create-failure
  (fn-traced [{:keys [db]} [_ {:keys [response]}]]
             {:db db}))

(re-frame/reg-event-fx
  ::fetch-all-transactions
  (fn-traced [{:keys [db]} _]
             {:http-xhrio {:method          :get
                           :uri             "http://localhost:8080/api/transactions"
                           :timeout         5000
                           :response-format (ajax/json-response-format {:keywords? true}) ;; IMPORTANT!: You must provide this.
                           :on-success      [::fetch-all-transactions-success]
                           :on-failure      [::create-failure]}}))

(re-frame/reg-event-fx
  ::fetch-all-transactions-success
  (fn-traced [{:keys [db]} [_ {:keys [transactions]}]]
             {:db (assoc db :transactions transactions)}))
