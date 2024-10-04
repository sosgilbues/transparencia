(ns transparencia.transaction.views
  (:require [re-frame.core :as re-frame]
            [transparencia.routes :as routes]
            [transparencia.subs :as subs]
            [transparencia.transaction.subs :as transaction-subs]
            [transparencia.transaction.events :as events]))

(defn transaction-details-panel []
  (let [{{:keys [route-params]} :route} @(re-frame/subscribe [::subs/active-panel])
        _ (re-frame/dispatch [::events/fetch-transaction (:id route-params)])
        transaction (re-frame/subscribe [::transaction-subs/transaction])]

    [:section.section
     [:div.container
      [:h1.title "Transação"]

      [:div.container

       [:div.field
        [:label.label "Título"]
        [:p (:title @transaction)]]

       [:div.field
        [:label.label "Data da Transação"]
        [:p (:reference-date @transaction)]]

       [:div.field
        [:label.label "Tipo de Transação"]
        [:p (:type @transaction)]]

       [:div.field
        [:label.label "Valor"]
        [:p (str "R$ " (:amount @transaction))]]

       [:div.field
        [:label.label "Descrição"]
        [:p (:description @transaction)]]]]]))

(defmethod routes/panels :transaction-details-panel [] [transaction-details-panel])
