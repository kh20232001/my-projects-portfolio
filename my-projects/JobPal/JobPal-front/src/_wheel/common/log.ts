const prodFlag = import.meta.env.VITE_REACT_APP_ACTIVE === 'prod';

const createLogHeader = () => {
  if (prodFlag) return;
  const date = new Date();
  let header = '[';
  header += `[${date.getFullYear()}-${paddingZero(date.getMonth() + 1)}-${paddingZero(date.getDate())}`;
  header += ' ';
  header += `${paddingZero(date.getHours())}:${paddingZero(date.getMinutes())}:${paddingZero(date.getSeconds())}`;
  header += '.';
  header += date.getMilliseconds();
  header += ']';
  return header;
};

const paddingZero = (v: number) => {
  return ('0' + v).slice(-2);
};

const info = (param: any) => {
  if (prodFlag) return;
  console.info(`${createLogHeader()} INFO : ${param}`);
};

const warn = (param: any) => {
  if (prodFlag) return;
  console.warn(`${createLogHeader()} WARN : ${param}`);
};

const error = (param: any) => {
  if (prodFlag) return;
  console.error(`${createLogHeader()} ERROR : ${param}`);
};

const trace = (param: any) => {
  if (prodFlag) return;
  console.trace(`${createLogHeader()} TRACE : ${param}`);
};

const logDict = { info, warn, error, trace };

export const log = () => logDict;
