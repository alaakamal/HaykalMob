package mypak;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.sql.Connection;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.util.JRLoader;

import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import org.apache.myfaces.trinidad.event.AttributeChangeEvent;

public class myclass {
    private RichInputText empno;
    private RichInputText yeart;
    private RichShowDetailItem v1;
    private RichShowDetailItem v2;
    private RichShowDetailItem v3;
    private RichPanelTabbed pt;
    private RichInputDate yeartD;
    private RichInputDate d1;
    private RichInputDate d2;
    private RichPopup showpopup;
    private RichPopup popup1;
    private RichInputText pass;
    private RichInputText nationalNo;

    public myclass() {
    }

    public Connection getDataSourceConnection(String dataSourceName) throws Exception {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup(dataSourceName);
        return ds.getConnection();
    }

    private Connection getConnection() throws Exception {
        return getDataSourceConnection("jdbc/HrXEDSak"); // use datasourse in your application
    }

    public ServletContext getContext() {
        return (ServletContext) getFacesContext().getExternalContext().getContext();
    }

    public HttpServletResponse getResponse() {
        return (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
    }

    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public void runReport(String repPath, java.util.Map param) throws Exception {
        Connection conn = null;
        try {
            HttpServletResponse response = getResponse();
            ServletOutputStream out = response.getOutputStream();
            response.setHeader("Cache-Control", "max-age=0");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachement; filename=\"ReportFile.pdf\"");
            ServletContext context = getContext();
            InputStream fs =
                context.getResourceAsStream("/reports/" +
                                            repPath); //we will put the report under folder "reports" under Web Content
            JasperReport template = (JasperReport) JRLoader.loadObject(fs);
            template.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
            conn = getConnection();
            JasperPrint print = JasperFillManager.fillReport(template, param, conn);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(print, baos);
            out.write(baos.toByteArray());
            out.flush();
            out.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception jex) {
            System.out.println(jex.toString());
            jex.printStackTrace();
        } finally {
            close(conn);
        }
    }

    @SuppressWarnings("oracle.jdeveloper.java.insufficient-catch-block")
    public void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }


    public static String getDisplayedDateTime() {
        Calendar cal = Calendar.getInstance();
        String dateFormat = "yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public void empinfo() {
        SimpleDateFormat sdfr0 = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfr = new SimpleDateFormat("MM/yyyy");
        BindingContainer bindings = getBindings();
        cleardata(0);
        cleardata(1);
        cleardata(2);

        if (v1.isDisclosed()) {

            if (getinfo(Integer.parseInt(empno.getValue().toString()), "01-" + sdfr.format(d1.getValue()),
                        "28-" + sdfr.format(d2.getValue()), Integer.parseInt(sdfr0.format(yeartD.getValue())), 1) > 0) {
                // v2.setDisclosed(false);
                // v3.setDisclosed(false);
                // v1.setDisclosed(true);
                AdfFacesContext.getCurrentInstance().addPartialTarget(pt);
                OperationBinding operationBinding = bindings.getOperationBinding("executeWithParams");
                Map paramsMap = operationBinding.getParamsMap();
                paramsMap.put("empNo", empno.getValue().toString());
                paramsMap.put("yearT1", sdfr0.format(yeartD.getValue()));
                operationBinding.execute();
            }
            /* if (getinfo(Integer.parseInt(empno.getValue().toString()), "01-" + sdfr.format(d1.getValue()),
                        "28-" + sdfr.format(d2.getValue()), Integer.parseInt(sdfr0.format(yeartD.getValue())), 1) ==
                0) { */
            else {

                v1.setDisclosed(false);
                v2.setDisclosed(false);
                v3.setDisclosed(true);
                AdfFacesContext.getCurrentInstance().addPartialTarget(pt);
                OperationBinding operationBinding = bindings.getOperationBinding("executeWithParams3");
                Map paramsMap = operationBinding.getParamsMap();
                paramsMap.put("empNo", empno.getValue().toString());
                operationBinding.execute();
                /*  System.out.println(nationalNo.getValue().toString());
                exv(4, nationalNo.getValue().toString());
                exv(5, nationalNo.getValue().toString());
                exv(6, nationalNo.getValue().toString()); */
            }
        }

        if (v2.isDisclosed()) {

            if (getinfo(Integer.parseInt(empno.getValue().toString()), "01-" + sdfr.format(d1.getValue()),
                        "28-" + sdfr.format(d2.getValue()), Integer.parseInt(sdfr0.format(yeartD.getValue())), 0) > 0) {
                OperationBinding operationBinding = bindings.getOperationBinding("executeWithParams2");
                Map paramsMap = operationBinding.getParamsMap();
                paramsMap.put("empNo", empno.getValue().toString());
                paramsMap.put("d1", "01-" + sdfr.format(d1.getValue()));
                paramsMap.put("d2", "28-" + sdfr.format(d2.getValue()));
                operationBinding.execute();
            }
            /* if (getinfo(Integer.parseInt(empno.getValue().toString()), "01-" + sdfr.format(d1.getValue()),
                        "28-" + sdfr.format(d2.getValue()), 0, 0) == 0) */
            else {

                v1.setDisclosed(false);
                v2.setDisclosed(false);
                v3.setDisclosed(true);
                AdfFacesContext.getCurrentInstance().addPartialTarget(pt);
                OperationBinding operationBinding = bindings.getOperationBinding("executeWithParams3");
                Map paramsMap = operationBinding.getParamsMap();
                paramsMap.put("empNo", empno.getValue().toString());
                operationBinding.execute();
                /*
                exv(4, nationalNo.getValue().toString());
                exv(5, nationalNo.getValue().toString());
                exv(6, nationalNo.getValue().toString());
*/

            }
        }
        if (v3.isDisclosed()) {
            cleardata(2);
            AdfFacesContext.getCurrentInstance().addPartialTarget(pt);
            OperationBinding operationBinding = bindings.getOperationBinding("executeWithParams3");
            Map paramsMap = operationBinding.getParamsMap();
            paramsMap.put("empNo", empno.getValue().toString());
            operationBinding.execute();
        }
    }

    public String b1_action() {
        // v2.setDisclosed(false);
        //v3.setDisclosed(false);
        //v1.setDisclosed(true);

        /* if (v3.isDisclosed()) {
            v1.setDisclosed(true);
        } */
        empinfo();
        // empno.resetValue();
        return null;
    }

    public void cleardata(Integer flage) {
        BindingContainer bindings = getBindings();
        if (flage == 0) {
            OperationBinding operationBinding = bindings.getOperationBinding("executeWithParams");
            Map paramsMap = operationBinding.getParamsMap();
            paramsMap.put("empNo", 0);
            paramsMap.put("yearT1", "2020");
            operationBinding.execute();
            System.out.println(flage);
        }
        if (flage == 1) {
            OperationBinding operationBinding = bindings.getOperationBinding("executeWithParams2");
            Map paramsMap = operationBinding.getParamsMap();
            paramsMap.put("empNo", 0);
            paramsMap.put("d1", "01-" + "01/2020");
            paramsMap.put("d2", "28-" + "01/2020");
            System.out.println(flage);
        }
        if (flage == 2) {
            OperationBinding operationBinding = bindings.getOperationBinding("executeWithParams3");
            Map paramsMap = operationBinding.getParamsMap();
            paramsMap.put("empNo", 0);
            operationBinding.execute();
            System.out.println(flage);
        }
    }

    public void exv(Integer flage, String national_no) {
        if (flage == 4) {
            BindingContainer bindings = getBindings();
            OperationBinding operationBinding = bindings.getOperationBinding("exvo4");
            Map paramsMap = operationBinding.getParamsMap();
            paramsMap.put("national_no", national_no);
            operationBinding.execute();
        }
        if (flage == 5) {
            BindingContainer bindings = getBindings();
            OperationBinding operationBinding = bindings.getOperationBinding("exvo5");
            Map paramsMap = operationBinding.getParamsMap();
            paramsMap.put("national_no", national_no);
            operationBinding.execute();
        }
        if (flage == 6) {
            BindingContainer bindings = getBindings();
            OperationBinding operationBinding = bindings.getOperationBinding("exvo6");
            Map paramsMap = operationBinding.getParamsMap();
            paramsMap.put("national_no", national_no);
            operationBinding.execute();
        }

    }

    public Integer getinfo(Integer empId, String d1, String d2, Integer YearT, Integer flage) {
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding = bindings.getOperationBinding("getBranchName");
        Map paramsMap = operationBinding.getParamsMap();
        paramsMap.put("empId", empId); ///, empno.getValue().toString());
        paramsMap.put("d1", d1); ///, "01-" + sdfr1.format(d1.getValue()));
        paramsMap.put("d2", d2); ///, "28-" + sdfr1.format(d2.getValue()));
        paramsMap.put("yeart", YearT); //, sdfr0.format(yeartD.getValue()));
        paramsMap.put("flage", flage);
        Object result = operationBinding.execute();
        return Integer.parseInt(result.toString());
    }

    public void setEmpno(RichInputText empno) {
        this.empno = empno;
    }

    public RichInputText getEmpno() {
        return empno;
    }

    public void setYeart(RichInputText yeart) {
        this.yeart = yeart;
    }

    public RichInputText getYeart() {
        return yeart;
    }

    public void setV1(RichShowDetailItem v1) {
        this.v1 = v1;
    }

    public RichShowDetailItem getV1() {
        return v1;
    }

    public void setV2(RichShowDetailItem v2) {
        this.v2 = v2;
    }

    public RichShowDetailItem getV2() {
        return v2;
    }

    public void setV3(RichShowDetailItem v3) {
        this.v3 = v3;
    }

    public RichShowDetailItem getV3() {
        return v3;
    }

    public String b2_action() {
        //v2.setDisclosed(true);
        //v2.setDisclosedTransient(true);

        //setDisclosedFirstTab(v2);
        return null;
    }


    public void setPt(RichPanelTabbed pt) {
        this.pt = pt;
    }

    public RichPanelTabbed getPt() {
        return pt;
    }


    public void setDisclosedFirstTab(RichShowDetailItem tabBind) {
        RichPanelTabbed richPanelTabbed = getPt();
        for (UIComponent child : richPanelTabbed.getChildren()) {
            RichShowDetailItem v2 = (RichShowDetailItem) child;
            if (v2.getClientId().equals(tabBind.getClientId())) {
                v3.setDisclosed(true);
            } else {
                v2.setDisclosed(false);
            }
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(pt);
    }

    public void setYeartD(RichInputDate yeartD) {
        this.yeartD = yeartD;
    }

    public RichInputDate getYeartD() {
        return yeartD;
    }

    public void setD1(RichInputDate d1) {
        this.d1 = d1;
    }

    public RichInputDate getD1() {
        return d1;
    }

    public void setD2(RichInputDate d2) {
        this.d2 = d2;
    }

    public RichInputDate getD2() {
        return d2;
    }

    public void clearv1(AttributeChangeEvent attributeChangeEvent) {
        cleardata(0);
        System.out.println(0);
        cleardata(1);
        System.out.println(1);
        cleardata(2);
        System.out.println(2);
    }

    public void clearv2(AttributeChangeEvent attributeChangeEvent) {
        cleardata(0);
        System.out.println(00);
        cleardata(1);
        System.out.println(11);
        cleardata(2);
        System.out.println(22);
    }

    public void clearv3(AttributeChangeEvent attributeChangeEvent) {
        cleardata(0);
        System.out.println(000);
        cleardata(1);
        System.out.println(111);
        cleardata(2);
        System.out.println(222);
    }


    public void printreport(ActionEvent actionEvent) {
        SimpleDateFormat sdfr0 = new SimpleDateFormat("yyyy");
        Map m = new HashMap();
        m.put("national", empno.getValue().toString());
        m.put("yearn", sdfr0.format(yeartD.getValue()));
        try {
            runReport("emp_taxes_fix_var.jasper", m);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void printreport2(ActionEvent actionEvent) {
        SimpleDateFormat sdfr = new SimpleDateFormat("MM/yyyy");
        Map m = new HashMap();
        m.put("NATIONAL", empno.getValue().toString());
        m.put("D_FR", "01-" + sdfr.format(d1.getValue()));
        m.put("D_TO", "28-" + sdfr.format(d2.getValue()));
        try {
            runReport("emp_pay_salary.jasper", m);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void printreport3(ActionEvent actionEvent) {
        Map m = new HashMap();
        m.put("national", empno.getValue().toString());
        try {
            runReport("emp_bian.jasper", m);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void setNationalNo(RichInputText nationalNo) {
        this.nationalNo = nationalNo;
    }

    public RichInputText getNationalNo() {
        return nationalNo;
    }

    public void clearVA(AttributeChangeEvent attributeChangeEvent) {
        cleardata(0);
        cleardata(1);
        cleardata(2);
    }
}
