(ns transparencia.login.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::login-form
  (fn [db _]
    (:login-form db)))

(re-frame/reg-sub
  ::access-token
  (fn [db _]
    (:access-token db)))

(re-frame/reg-sub
  ::login-result
  (fn [db _]
    (:login-result db)))
