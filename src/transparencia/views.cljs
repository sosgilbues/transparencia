(ns transparencia.views
  (:require
    [re-frame.core :as re-frame]
    [transparencia.events :as events]
    [transparencia.routes :as routes]
    [transparencia.subs :as subs]
    [transparencia.login.views]
    [transparencia.manage.views]
    [transparencia.transaction.views]))

;; home

(defn home-panel []
  [:div.container
   [:h1.title
    "Página Principal"]

   [:div.container
    [:button.button {:on-click #(routes/navigate! :login)}
     "Login"]]])

(defmethod routes/panels :home-panel [] [home-panel])

;; main

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (routes/panels (:panel @active-panel))))
