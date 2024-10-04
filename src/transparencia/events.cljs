(ns transparencia.events
  (:require
    [re-frame.core :as re-frame]
    [transparencia.db :as db]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [day8.re-frame.http-fx]
    [ajax.core :as ajax]))

(re-frame/reg-event-db
  ::initialize-db
  (fn-traced [_ _]
    db/default-db))

(re-frame/reg-event-fx
  ::navigate
  (fn-traced [_ [_ handler]]
    {:navigate handler}))

(re-frame/reg-event-fx
  ::set-active-panel
  (fn-traced [{:keys [db]} [_ active-panel]]
    {:db (assoc db :active-panel active-panel)}))

(re-frame/reg-event-fx
  ::update-form
  (fn-traced [{:keys [db]} [_ id value]]
    {:db (assoc-in db [:login-form id] value)}))

(re-frame/reg-event-fx                                      ;; note the trailing -fx
  ::login                                                   ;; usage:  (dispatch [:handler-with-http])
  (fn-traced [{:keys [db]} _]
    (println (-> db :login-form :password))        ;; the first param will be "world"
    {:http-xhrio {:method          :post
                  :uri             "https://api-ong-auth.sosgilbues.org.br/api/customers/auth"
                  :params          {:username (-> db :login-form :username)
                                    :password (-> db :login-form :password)}
                  :timeout         5000
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true}) ;; IMPORTANT!: You must provide this.
                  :on-success      [::login-success]
                  :on-failure      [::login-failure]}}))

(re-frame/reg-event-fx
  ::login-success
  (fn-traced [{:keys [db]} [_ {:keys [token]}]]
    {:db (assoc db :access-token token)}))

(re-frame/reg-event-fx
  ::login-failure
  (fn-traced [{:keys [db]} [_ {:keys [response]}]]
    {:db (assoc db :login-result (keyword (:error response)))}))
