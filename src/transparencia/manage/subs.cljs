(ns transparencia.manage.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::transactions
  (fn [db _]
    (:transactions db)))
