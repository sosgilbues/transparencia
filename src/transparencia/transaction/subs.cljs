(ns transparencia.transaction.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::transaction
  (fn [db _]
    (:transaction db)))
