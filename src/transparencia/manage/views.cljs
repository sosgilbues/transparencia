(ns transparencia.manage.views
  (:require [re-frame.core :as re-frame]
            [transparencia.routes :as routes]
            [transparencia.login.subs :as subs]
            [transparencia.manage.subs :as manage-subs]
            [transparencia.manage.events :as events]))

(defn transactions []
  (let [transactions (re-frame/subscribe [::manage-subs/transactions])]
    [:section.section
     [:div.container
      [:h1.subtitle "Transações"]
      [:table.table.is-fullwidth.is-bordered.is-striped
       [:thead
        [:tr
         [:th "Título"]
         [:th "Valor"]
         [:th "Data"]]]
       [:tbody
        (map (fn [transaction]
               [:tr
                [:td [:a {:href (str "/transaction-details/" (:id transaction))} (:title transaction)]]
                [:td (str "R$ " (:amount transaction))]
                [:td (:reference-date transaction)]]) @transactions)]]]]))

(defn manage-panel []
  (when-not @(re-frame/subscribe [::subs/access-token])
    (routes/navigate! :login))

  (re-frame/dispatch [::events/fetch-all-transactions])

  (let []
    [:section/section
     [:div.container
      [:h1.title
       "Gerenciar Transações"]

      [:button.button {:on-click #(routes/navigate! :add-transaction)} "Cadastrar Nova Transação"]]

     (transactions)]))

(defmethod routes/panels :manage-panel [] [manage-panel])

(defn add-transaction-panel []
  (when-not @(re-frame/subscribe [::subs/access-token])
    (routes/navigate! :login))

  (re-frame/dispatch [::events/fetch-all-transactions])

  [:section.section
   [:div.container
    [:h1.title "Cadastrar Nova Transação"]

    [:div.container

     [:div.field
      [:label.label "Título"]
      [:div.control
       [:input.input {:type      "text"
                      :on-change #(re-frame/dispatch [::events/update-form :title (-> % .-target .-value)])}]]]

     [:div.field
      [:label.label "Data da Transação"]
      [:div.control
       [:input.input {:type      "date"
                      :on-change #(re-frame/dispatch [::events/update-form :reference-date (-> % .-target .-value)])}]]]

     [:div.field
      [:label.label "Tipo de Transação"]
      [:div.select.is-rounded
       [:select
        {:on-change #(re-frame/dispatch [::events/update-form :type (-> % .-target .-value)])}
        [:option {:value "credit"} "Crédito"]
        [:option {:value "debit"} "Débito"]]]]

     [:div.field
      [:label.label "Valor"]
      [:div.control
       [:input.input {:type      "number"
                      :on-change #(re-frame/dispatch [::events/update-form :amount (-> % .-target .-value)])}]]]

     [:div.field
      [:label.label "Descrição"]
      [:div.control
       [:textarea.textarea
        {:on-change #(re-frame/dispatch [::events/update-form :description (-> % .-target .-value)])}]]]

     [:button.button {:type     "submit"
                      :on-click #(re-frame/dispatch [::events/create-transaction])} "Cadastrar"]]]

   (transactions)])

(defmethod routes/panels :add-transaction-panel [] [add-transaction-panel])
