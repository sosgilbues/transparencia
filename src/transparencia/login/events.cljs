(ns transparencia.login.events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [transparencia.login.adapters :as login.adapters]))

(re-frame/reg-event-fx
  ::update-form
  (fn-traced [{:keys [db]} [_ id value]]
             {:db (assoc-in db [:login-form id] value)}))

(re-frame/reg-event-fx
  ::login
  (fn-traced [{:keys [db]} _]
             {:http-xhrio {:method          :post
                           :uri             "http://localhost:8080/api/customers/auth"
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
             {:db (assoc db :access-token token
                            :customer (login.adapters/access-toke->customer token))}))

(re-frame/reg-event-fx
  ::login-failure
  (fn-traced [{:keys [db]} [_ {:keys [response]}]]
             {:db (assoc db :login-result (keyword (:error response)))}))
